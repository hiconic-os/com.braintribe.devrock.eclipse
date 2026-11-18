#!/usr/bin/env node
/** CI bootstrap + historical hc update-classpath + the verified local p2 builder. */
import { parseArgs } from 'node:util';
import { fileURLToPath } from 'node:url';
import { dirname, resolve, join } from 'node:path';
import { mkdir, readFile, writeFile, copyFile, cp, readdir, access } from 'node:fs/promises';
import { createWriteStream } from 'node:fs';
import { spawn } from 'node:child_process';
import { release } from './release.mjs';
import { prepareWebsite } from './website.mjs';
import { vendoredSdk } from './vendor.mjs';
const tools=resolve(dirname(fileURLToPath(import.meta.url)),'..'),repo=resolve(tools,'..');
const modules=JSON.parse(await readFile(join(tools,'deployment/modules.json'),'utf8'));
const exists=async p=>{try{await access(p);return true;}catch{return false;}};
async function run(command,args,log,cwd=repo){
 await mkdir(dirname(log),{recursive:true});
 const out=createWriteStream(log);const child=spawn(command,args,{cwd,stdio:['ignore','pipe','pipe']});child.stdout.pipe(out,{end:false});child.stderr.pipe(out,{end:false});
 const code=await new Promise((ok,bad)=>{child.once('error',bad);child.once('close',ok);});await new Promise(ok=>out.end(ok));
 if(code!==0)throw new Error(`${command} failed (${code}); inspect ${log}`);
}
export async function build({work,baseline,refresh=true,hc='hc',publishedSite}){
 work=resolve(work);await mkdir(work,{recursive:true});const lock=JSON.parse(await readFile(join(tools,'deployment/baseline.json'),'utf8'));
 const site=new URL(publishedSite??lock.baselineSite);
 if(site.protocol!=='https:'||site.username||site.password||site.search||site.hash)throw new Error('Published site must be a plain HTTPS directory URL');
 if(!site.pathname.endsWith('/'))site.pathname+='/';
 console.log('Published site snapshot: '+site.href);
 const logs=join(work,'bootstrap-logs');await mkdir(logs,{recursive:true});
 const published=join(work,'published');await mkdir(published,{recursive:true});
 for(const name of ['content.jar','artifacts.jar']){
  const response=await fetch(new URL(name,site),{signal:AbortSignal.timeout(30_000)});
  if(!response.ok)throw new Error('Cannot snapshot current site metadata: '+name);
  await writeFile(join(published,name),Buffer.from(await response.arrayBuffer()));
 }

 if(!baseline)baseline=await vendoredSdk(work);
 baseline=resolve(baseline);
 let prepared;
 if(refresh){
  // The public beta embeds a newer rc model than the published Maven model.
  // Build this checkout's helper artifacts before resolving each plugin closure.
  for(const helper of ['parent','devrock-plugin-model','devrock-plugin-reason-model','devrock-reason-transposition','devrock-resolution-transposition']){
   console.log('hc install helper: '+helper);
   const dest=join(work,'helpers',helper);
   await cp(join(repo,helper),dest,{recursive:true,filter:source=>!['build','dist','classes','class-gen','node_modules','.git'].includes(source.split('/').at(-1))});
   await run(hc,['install'],join(logs,helper+'-install.log'),dest);
  }
  prepared=join(work,'dependencies');
  for(const item of modules.filter(item=>item.prepareWithAnt)){
   const module=item.folder;
   console.log('hc update-classpath: '+module);
   const dest=join(prepared,module);await mkdir(dest,{recursive:true});
   for(const name of ['pom.xml','update.xml'])await copyFile(join(repo,module,name),join(dest,name));
   await cp(join(repo,module,'res-dev'),join(dest,'res-dev'),{recursive:true});
   await run(hc,['update-classpath','--buildFile','update.xml'],join(logs,module+'-dependencies.log'),dest);
  }
 }
 const output=join(work,'build');
 let buildQualifier;
 if(process.env.GITHUB_RUN_NUMBER){const stamp=new Date().toISOString().replace(/[-:T.Z]/g,'').slice(0,14);buildQualifier=stamp+'_r'+process.env.GITHUB_RUN_NUMBER.padStart(10,'0')+'_a'+(process.env.GITHUB_RUN_ATTEMPT??'1').padStart(4,'0');}
 console.log('Build plugin source, publish p2 metadata and install into an isolated smoke profile.');
 await release({eclipse:baseline,output,dependenciesFrom:prepared,published,buildQualifier});
 await prepareWebsite({build:output,previous:published,output:join(work,'website')});
 await writeFile(join(work,'ci-build.json'),JSON.stringify({baseline,publishedSite:site.href,dependencies:prepared??'baseline',site:join(output,'site')},null,2)+'\n');
 console.log('Verified p2 site: '+join(output,'site'));
 return output;
}
if(process.argv[1]&&resolve(process.argv[1])===fileURLToPath(import.meta.url)){
 const {values}=parseArgs({options:{'published-site':{type:'string'},work:{type:'string'},baseline:{type:'string'},'baseline-dependencies':{type:'boolean',default:false},hc:{type:'string',default:'hc'}}});
 if(!values.work)throw new Error('--work is required');
 build({work:values.work,baseline:values.baseline,publishedSite:values['published-site'],refresh:!values['baseline-dependencies'],hc:values.hc}).catch(error=>{console.error(error.message);process.exitCode=1;});
}
