#!/usr/bin/env node
import {readFile} from 'node:fs/promises';
import {resolve,join} from 'node:path';
import {spawn} from 'node:child_process';
import {parseArgs} from 'node:util';
import {exists,uri,isMain,cli} from './common.mjs';
async function execute(command,args,capture=false){return new Promise((ok,bad)=>{let text='';const child=spawn(command,args,{stdio:capture?['ignore','pipe','pipe']:'inherit'});if(capture){child.stdout.on('data',b=>text+=b);child.stderr.on('data',b=>text+=b);}child.once('error',bad);child.once('close',code=>code===0?ok(text):bad(new Error(`p2 Director failed (${code}). Close the target Eclipse first.\n${text}`)));});}
export async function install({eclipse,site}){
 eclipse=resolve(eclipse);let repository,report;
 if(/^https?:\/\//.test(site)){repository=site.replace(/\/?$/,'/');const r=await fetch(repository+'build-report.json',{signal:AbortSignal.timeout(30_000)});if(!r.ok)throw new Error('Cannot read site build report');report=await r.json();}
 else{site=resolve(site);if(!await exists(join(site,'content.jar')))throw new Error('Expected a built p2 site');repository=uri(site);report=JSON.parse(await readFile(join(site,'../build-report.json'),'utf8'));}
 const executable=join(eclipse,process.platform==='win32'?'eclipsec.exe':'eclipse');if(!await exists(executable))throw new Error('Eclipse launcher missing');
 const common=['-nosplash','-consolelog','-application','org.eclipse.equinox.p2.director'],listing=await execute(executable,[...common,'-listInstalledRoots'],true);
 const present=new Set(listing.split(/\r?\n/).map(l=>l.split('/')[0].trim())),items=Object.values(report.modules),remove=items.map(m=>m.feature+'.feature.group').filter(id=>present.has(id));
 const args=[...common,'-repository',repository,'-installIU',items.map(m=>m.feature+'.feature.group/'+m.featureVersion).join(','),'-tag','Devrock-'+report.qualifier];if(remove.length)args.push('-uninstallIU',remove.join(','));
 const url=new URL(repository);if(url.protocol==='http:'&&['localhost','127.0.0.1','[::1]'].includes(url.hostname))args.push('-vmargs','-Dp2.httpRule=allow');
 console.log('Updating the closed target Eclipse: '+eclipse);await execute(executable,args);console.log('Update completed. Start Eclipse normally.');
}
if(isMain(import.meta.url))cli(async()=>{const {values}=parseArgs({options:{eclipse:{type:'string'},site:{type:'string'}}});if(!values.eclipse||!values.site)throw new Error('--eclipse and --site are required');await install(values);});
