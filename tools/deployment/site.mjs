#!/usr/bin/env node
import {readFile,writeFile,mkdir,cp,rename,readdir,stat,realpath,open,unlink} from 'node:fs/promises';
import {createReadStream} from 'node:fs';
import {createServer} from 'node:http';
import {join,resolve,extname} from 'node:path';
import {parseArgs} from 'node:util';
import {exists,document,append,xml,jsonFile,isMain,cli} from './common.mjs';
export async function publishSite({root,site}){
 root=resolve(root);site=resolve(site);const report=JSON.parse(await readFile(join(site,'../build-report.json'),'utf8')),name=report.qualifier;
 if(!/^[A-Za-z0-9_-]+$/.test(name))throw new Error('Invalid release qualifier');
 if(!await exists(join(site,'content.jar'))||!await exists(join(site,'artifacts.jar')))throw new Error('Expected a built p2 site');
 await mkdir(join(root,'releases'),{recursive:true});const lock=await open(join(root,'.publication.lock'),'wx');
 try{
  const target=join(root,'releases',name);if(await exists(target))throw new Error('Release exists and is immutable: '+target);
  await cp(site,target,{recursive:true,errorOnExist:true,force:false});await jsonFile(join(target,'build-report.json'),report);
  const children=[];for(const item of await readdir(join(root,'releases'),{withFileTypes:true}))if(item.isDirectory()&&await exists(join(root,'releases',item.name,'content.jar')))children.push('releases/'+item.name);children.sort();
  for(const [kind,type] of [['Artifacts','org.eclipse.equinox.internal.p2.artifact.repository.CompositeArtifactRepository'],['Content','org.eclipse.equinox.internal.p2.metadata.repository.CompositeMetadataRepository']]){
   const doc=document('repository',{name:'Devrock local releases',type,version:'1.0.0'}),node=doc.documentElement;append(append(node,'properties',{size:1}),'property',{name:'p2.timestamp',value:Date.now()});const list=append(node,'children',{size:children.length});for(const location of children)append(list,'child',{location});const path=join(root,'composite'+kind+'.xml');await writeFile(path+'.next',xml(doc));await rename(path+'.next',path);
  }
  await jsonFile(join(root,'build-report.next'),report);await rename(join(root,'build-report.next'),join(root,'build-report.json'));
  await writeFile(join(root,'p2.index'),'version = 1\nmetadata.repository.factory.order = compositeContent.xml,\\!\nartifact.repository.factory.order = compositeArtifacts.xml,\\!\n');
  console.log(`Published ${name}; retained ${children.length} releases.`);return children;
 }finally{await lock.close();await unlink(join(root,'.publication.lock'));}
}
const escape=s=>s.replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
export function server(root){root=resolve(root);return createServer((req,res)=>{(async()=>{
 if(!['GET','HEAD'].includes(req.method)){res.writeHead(405,{Allow:'GET, HEAD'}).end();return;}
 const url=new URL(req.url,'http://localhost'),path=resolve(root,'.'+decodeURIComponent(url.pathname));if(path!==root&&!path.startsWith(root+'/')){res.writeHead(403).end();return;}
 const actual=await realpath(path);if(actual!==root&&!actual.startsWith(root+'/')){res.writeHead(403).end();return;}
 const info=await stat(actual);res.setHeader('Cache-Control',url.pathname.startsWith('/releases/')?'public, max-age=31536000, immutable':'no-cache');
 if(info.isDirectory()){
  if(await exists(join(actual,'index.html'))){const body=await readFile(join(actual,'index.html'));res.writeHead(200,{'Content-Type':'text/html; charset=utf-8','Content-Length':body.length}).end(req.method==='HEAD'?undefined:body);return;}
  const names=await readdir(actual);const body=Buffer.from('<!doctype html><meta charset="utf-8"><title>Eclipse update site</title><h1>Eclipse update site</h1><ul>'+names.filter(n=>!n.startsWith('.')).sort().map(n=>'<li><a href="'+escape(url.pathname.replace(/\/?$/,'/')+encodeURIComponent(n))+'">'+escape(n)+'</a></li>').join('')+'</ul>');res.writeHead(200,{'Content-Type':'text/html; charset=utf-8','Content-Length':body.length}).end(req.method==='HEAD'?undefined:body);return;
 }
 const mime={'.xml':'application/xml','.json':'application/json','.jar':'application/java-archive','.html':'text/html; charset=utf-8','.css':'text/css','.index':'text/plain'}[extname(actual)]??'application/octet-stream';res.writeHead(200,{'Content-Type':mime,'Content-Length':info.size});if(req.method==='HEAD')res.end();else createReadStream(actual).on('error',()=>res.destroy()).pipe(res);
 })().catch(error=>{if(!res.headersSent)res.writeHead(error.code==='ENOENT'?404:500);res.end();});});}
if(isMain(import.meta.url))cli(async()=>{const {values,positionals}=parseArgs({allowPositionals:true,options:{root:{type:'string'},site:{type:'string'},port:{type:'string',default:'8098'}}});if(!values.root)throw new Error('--root is required');if(positionals[0]==='publish'){if(!values.site)throw new Error('--site is required');await publishSite({root:values.root,site:values.site});}else if(positionals[0]==='serve'){if(!await exists(join(values.root,'compositeContent.xml')))throw new Error('Publish a release first');const port=Number(values.port);await new Promise((ok,bad)=>{const http=server(values.root);http.once('error',bad);http.listen(port,'127.0.0.1',ok);});console.log('Eclipse update URL: http://127.0.0.1:'+port+'/');}else throw new Error('Use publish or serve');});
