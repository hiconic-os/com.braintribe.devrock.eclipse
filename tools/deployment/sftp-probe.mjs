#!/usr/bin/env node
import SftpClient from 'ssh2-sftp-client';
import { parseArgs, parseEnv } from 'node:util';
import { randomUUID, createHash } from 'node:crypto';
import { readFile, writeFile, mkdir, rename } from 'node:fs/promises';
import { dirname, posix, resolve } from 'node:path';
import { pathToFileURL } from 'node:url';

export function validate(options) {
  if(!options.host||!/^[a-zA-Z0-9.-]+$/.test(options.host))throw new Error('A DNS hostname is required.');
  if(!Number.isInteger(options.port??22)||(options.port??22)<1||(options.port??22)>65535)throw new Error('Invalid SFTP port.');
  if(!options.remoteDir?.startsWith('/')||/[\r\n\\]/.test(options.remoteDir)||options.remoteDir.split('/').includes('..'))throw new Error('Use an explicit absolute SFTP destination without traversal.');
  if(!options.hostKey&&!options.knownHosts)throw new Error('Specify --host-key or a local --known-hosts file to pin the first observed SSH host key.');
  if(options.hostKey&&!/^[a-f0-9]{64}$/i.test(options.hostKey))throw new Error('Host key must be its SHA-256 hex fingerprint.');
  if(options.publicBase){const u=new URL(options.publicBase);if(u.protocol!=='https:'||u.username||u.password||u.search||u.hash)throw new Error('Public base must be an HTTPS directory URL.');}
}
const digest=bytes=>createHash('sha256').update(bytes).digest('hex');
export async function probe(options,{client=new SftpClient(),user=process.env.ECLIPSE_FTP_USER,password=process.env.ECLIPSE_FTP_PASSWORD,fetcher=fetch}={}) {
  validate(options);if(!user||!password)throw new Error('Fill ECLIPSE_FTP_USER and ECLIPSE_FTP_PASSWORD in the private env file.');
  const peer=options.host+':'+(options.port??22);let known={};
  if(options.knownHosts)try{known=JSON.parse(await readFile(options.knownHosts,'utf8'));}catch(e){if(e.code!=='ENOENT')throw e;}
  const expected=options.hostKey??known[peer];let observed;
  const token=randomUUID();
  const relative=options.html?'deployment-test-'+token+'.html':'_deployment-probe/probe-'+token+'.txt';
  const target=posix.join(options.remoteDir,relative),temporary=target+'.uploading';
  const now=new Date().toISOString();
  const bytes=Buffer.from(options.html?`<!doctype html><html lang="de"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Hiconic Deployment-Test</title><style>body{font:17px/1.65 system-ui,sans-serif;color:#263b31;background:white;max-width:720px;margin:12vh auto;padding:24px}h1{color:#236846;font-size:32px}code{font-size:14px;overflow-wrap:anywhere}.tag{border-left:4px solid #d8b44b;padding-left:18px;color:#68766d}</style><h1>SFTP-Deployment erfolgreich</h1><p>Diese Testseite wurde automatisiert mit Node.js auf den IONOS-Webspace geladen.</p><p>Der produktive Eclipse-Updatebereich wurde nicht verändert.</p><p class="tag">Test-ID: <code>${token}</code><br>Erstellt: <time>${now}</time></p></html>`:'Hiconic Eclipse SFTP deployment probe\n'+relative+'\n'+now+'\n');
  let uploaded=false,renamed=false,verified=false;
  try {
    await client.connect({host:options.host,port:options.port??22,username:user,password,readyTimeout:30_000,hostHash:'sha256',hostVerifier:fingerprint=>{observed=fingerprint;return !expected||fingerprint.toLowerCase()===expected.toLowerCase();}});
    if(!observed)throw new Error('SSH did not supply a host key fingerprint.');
    if(!expected){
      known[peer]=observed;await mkdir(dirname(options.knownHosts),{recursive:true});
      const temp=options.knownHosts+'.next';await writeFile(temp,JSON.stringify(known,null,2)+'\n',{mode:0o600});await rename(temp,options.knownHosts);
    }
    const root=await client.exists(options.remoteDir);if(root&&root!=='d')throw new Error('SFTP destination is not a directory.');
    if(!root)await client.mkdir(options.remoteDir,true);
    await client.mkdir(posix.dirname(target),true);
    uploaded=true;await client.put(bytes,temporary);await client.rename(temporary,target);renamed=true;
    if(digest(await client.get(target))!==digest(bytes))throw new Error('SFTP checksum readback failed.');
    if(options.publicBase){const u=new URL(relative,options.publicBase.replace(/\/?$/,'/'));const r=await fetcher(u,{cache:'no-store',signal:AbortSignal.timeout(30_000)});if(!r.ok||digest(Buffer.from(await r.arrayBuffer()))!==digest(bytes))throw new Error('Public HTTPS verification failed; check document-root mapping.');}
    verified=true;return {relativePath:relative,sha256:digest(bytes),hostKey:observed,firstSeen:!expected,httpsVerified:!!options.publicBase,retained:!!options.keep};
  } finally {
    try{if(uploaded&&(!options.keep||!verified))await client.delete(renamed?target:temporary,true);}finally{await client.end();}
  }
}

async function main(){
 const {values}=parseArgs({options:{host:{type:'string'},port:{type:'string',default:'22'},'remote-dir':{type:'string'},'public-base':{type:'string'},'env-file':{type:'string'},'known-hosts':{type:'string'},'host-key':{type:'string'},keep:{type:'boolean',default:false},html:{type:'boolean',default:false}}});
 if(values['env-file']){const parsed=parseEnv(await readFile(values['env-file'],'utf8'));for(const key of ['ECLIPSE_FTP_USER','ECLIPSE_FTP_PASSWORD'])if(key in parsed)process.env[key]=parsed[key];}
 const result=await probe({host:values.host,port:Number(values.port),remoteDir:values['remote-dir'],publicBase:values['public-base'],knownHosts:values['known-hosts']?resolve(values['known-hosts']):undefined,hostKey:values['host-key'],keep:values.keep,html:values.html});
 console.log(`SFTP upload, rename and checksum readback passed; HTTPS check: ${result.httpsVerified?'passed':'not requested'}; test file: ${result.retained?'retained':'removed'}.`);
 console.log(`SSH host key SHA-256 (hex): ${result.hostKey}${result.firstSeen?' (first-use pin)':''}`);
 if(result.retained)console.log(values['public-base']?new URL(result.relativePath,values['public-base'].replace(/\/?$/,'/')).href:result.relativePath);
}
if(import.meta.url===pathToFileURL(process.argv[1]??'').href)main().catch(error=>{
 let message=error instanceof Error?error.message:'SFTP probe failed';for(const v of [process.env.ECLIPSE_FTP_USER,process.env.ECLIPSE_FTP_PASSWORD])if(v)message=message.split(v).join('[redacted]');console.error(message);process.exitCode=1;
});
