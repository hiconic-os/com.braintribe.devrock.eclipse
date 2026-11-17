#!/usr/bin/env node
/** Publish a prepared cumulative site. Read-only unless --apply is explicit. */
import SftpClient from 'ssh2-sftp-client';
import {readFile} from 'node:fs/promises';
import {resolve,posix} from 'node:path';
import {createHash,randomUUID} from 'node:crypto';
import {parseArgs,parseEnv} from 'node:util';
import {pathToFileURL} from 'node:url';
const hash=bytes=>createHash('sha256').update(bytes).digest('hex');
function relative(path){if(!path||path.startsWith('/')||/[\r\n\\]/.test(path)||path.split('/').some(p=>p==='..'||p==='.'||!p))throw new Error('Unsafe publication path');return path;}
export async function publish(client,{source,root,apply=false,publicBase,fetcher=fetch}){
 if(!root?.startsWith('/')||/[\r\n\\]/.test(root)||root.split('/').includes('..'))throw new Error('Explicit absolute remote root required');
 if(publicBase){const u=new URL(publicBase);if(u.protocol!=='https:'||u.username||u.password||u.search||u.hash)throw new Error('Public base must be a plain HTTPS directory URL');}
 const plan=JSON.parse(await readFile(resolve(source,'publication-plan.json'),'utf8'));
 if(!/^[A-Za-z0-9_-]+$/.test(plan.qualifier))throw new Error('Invalid release qualifier');
 const payloads=plan.payloadsFirst,metadata=plan.metadataLast;
 if(metadata.map(x=>x.path).join(',')!=='artifacts.jar,content.jar,site.xml,index.html')throw new Error('Unsafe metadata activation order');
 for(const item of [...payloads,...metadata]){
  relative(item.path);
  if(payloads.includes(item)&&!/^((plugins|features)\/[^/]+\.jar)$/.test(item.path))throw new Error('Payload must be a versioned feature or plugin JAR');
  const data=await readFile(resolve(source,item.path));if(hash(data)!==item.sha256||data.length!==item.size)throw new Error('Local candidate changed: '+item.path);
 }
 if(await client.exists(root)!=='d')throw new Error('Remote site root must already exist');
 async function fence(){for(const [name,previous] of Object.entries(plan.previousMetadata)){
  if(!['content.jar','artifacts.jar'].includes(name))throw new Error('Invalid publication fence');
  const current=hash(await client.get(posix.join(root,name))),next=metadata.find(m=>m.path===name).sha256;
  if(current!==previous&&current!==next)throw new Error('Server metadata changed; rebuild the cumulative candidate before publishing');
 }}
 await fence();
 for(const path of plan.retainedPayloads??[]){relative(path);if(!await client.exists(posix.join(root,path)))throw new Error('Legacy payload missing on server: '+path);}
 if(!apply)return {applied:false,payloads:payloads.length,metadata:metadata.length,qualifier:plan.qualifier};
 const owner=randomUUID(),lock=posix.join(root,'.p2-publication.lock');let acquired=false;
 const temporary=[];
 try{
  // SSH_FXF_EXCL prevents concurrent cooperating publishers from both taking the lock.
  await client.put(Buffer.from(owner),lock,{writeStreamOptions:{flags:'wx'}});acquired=true;await fence();
  for(const item of payloads){
   const target=posix.join(root,item.path);
   if(await client.exists(target)){if(hash(await client.get(target))!==item.sha256)throw new Error('Immutable server payload differs: '+item.path);continue;}
   await client.mkdir(posix.dirname(target),true);
   const staging=target+'.'+owner+'.uploading';temporary.push(staging);
   await client.put(resolve(source,item.path),staging);
   if(hash(await client.get(staging))!==item.sha256)throw new Error('Uploaded payload checksum differs: '+item.path);
   await client.rename(staging,target);
  }
  // Stage all mutable files before activating any of them.
  const staged=[];
  for(const item of metadata){const target=posix.join(root,item.path),staging=target+'.'+owner+'.uploading';temporary.push(staging);await client.put(resolve(source,item.path),staging);if(hash(await client.get(staging))!==item.sha256)throw new Error('Uploaded metadata checksum differs: '+item.path);staged.push([staging,target]);}
  await fence();
  const backup=posix.join(root,'_publication-history',plan.qualifier);await client.mkdir(backup,true);
  for(const item of metadata){const original=posix.join(root,item.path),saved=posix.join(backup,item.path);if(await client.exists(original)&&!await client.exists(saved))await client.put(await client.get(original),saved);}
  // The new artifacts inventory is a superset, so a failure before content activation
  // leaves the previous installable-unit inventory usable. Existing payloads stay put.
  for(const [staging,target] of staged)await client.posixRename(staging,target);
  if(publicBase)for(const item of metadata){const url=new URL(item.path,publicBase.replace(/\/?$/,'/'));url.searchParams.set('deployment',plan.qualifier);const response=await fetcher(url,{cache:'no-store',signal:AbortSignal.timeout(30_000)});if(!response.ok||hash(Buffer.from(await response.arrayBuffer()))!==item.sha256)throw new Error('Published HTTPS metadata verification failed: '+item.path);}
  return {applied:true,payloads:payloads.length,metadata:metadata.length,qualifier:plan.qualifier};
 }finally{
  if(acquired){for(const path of temporary)if(await client.exists(path))await client.delete(path,true);if((await client.get(lock)).toString()===owner)await client.delete(lock);}
 }
}
async function main(){
 const {values}=parseArgs({options:{source:{type:'string'},host:{type:'string'},port:{type:'string',default:'22'},'remote-dir':{type:'string'},'host-key':{type:'string'},'env-file':{type:'string'},'public-base':{type:'string'},apply:{type:'boolean',default:false}}});
 if(!values.source||!values.host||!/^[a-f0-9]{64}$/i.test(values['host-key']??''))throw new Error('--source, --host and pinned --host-key are required');
 if(values['env-file']){const env=parseEnv(await readFile(values['env-file'],'utf8'));for(const key of ['ECLIPSE_FTP_USER','ECLIPSE_FTP_PASSWORD'])if(key in env)process.env[key]=env[key];}
 if(!process.env.ECLIPSE_FTP_USER||!process.env.ECLIPSE_FTP_PASSWORD)throw new Error('Deployment credentials are missing');
 const client=new SftpClient();try{
  await client.connect({host:values.host,port:Number(values.port),username:process.env.ECLIPSE_FTP_USER,password:process.env.ECLIPSE_FTP_PASSWORD,readyTimeout:30_000,hostHash:'sha256',hostVerifier:key=>key.toLowerCase()===values['host-key'].toLowerCase()});
  const result=await publish(client,{source:resolve(values.source),root:values['remote-dir'],apply:values.apply,publicBase:values['public-base']});console.log(JSON.stringify(result));
 }finally{await client.end();}
}
if(import.meta.url===pathToFileURL(process.argv[1]??'').href)main().catch(e=>{let text=e.message;for(const v of [process.env.ECLIPSE_FTP_USER,process.env.ECLIPSE_FTP_PASSWORD])if(v)text=text.split(v).join('[redacted]');console.error(text);process.exitCode=1;});
