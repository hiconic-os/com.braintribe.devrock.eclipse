import {readFile,writeFile,mkdir,readdir,access} from 'node:fs/promises';
import {createReadStream,createWriteStream} from 'node:fs';
import {createHash} from 'node:crypto';
import {spawn} from 'node:child_process';
import {dirname,join,resolve} from 'node:path';
import {fileURLToPath,pathToFileURL} from 'node:url';
import {DOMParser,XMLSerializer} from '@xmldom/xmldom';
export const repo=resolve(dirname(fileURLToPath(import.meta.url)),'../..');
export const tools=join(repo,'tools');
export const modules=JSON.parse(await readFile(join(tools,'deployment/modules.json'),'utf8'));
export const exists=async path=>{try{await access(path);return true;}catch{return false;}};
export const uri=path=>pathToFileURL(resolve(path)).href;
export const isMain=url=>!!process.argv[1]&&url===pathToFileURL(resolve(process.argv[1])).href;
export function cli(fn){fn().catch(error=>{console.error('ERROR: '+error.message);process.exitCode=1;});}
export async function run(command,args,log,cwd=repo){
 await mkdir(dirname(log),{recursive:true});const out=createWriteStream(log);
 try{await new Promise((ok,bad)=>{const child=spawn(command,args,{cwd,stdio:['ignore','pipe','pipe']});child.stdout.pipe(out,{end:false});child.stderr.pipe(out,{end:false});child.once('error',bad);child.once('close',code=>code===0?ok():bad(new Error(`${command} failed (${code}); inspect ${log}`)));});}
 finally{await new Promise(ok=>out.end(ok));}
}
export async function hashFile(path,algorithm='sha256'){const hash=createHash(algorithm);for await(const part of createReadStream(path))hash.update(part);return hash.digest('hex');}
export const hashBytes=bytes=>createHash('sha256').update(bytes).digest('hex');
export async function files(root){if(!await exists(root))return [];const result=[];for(const item of (await readdir(root,{withFileTypes:true})).sort((a,b)=>a.name.localeCompare(b.name,'en'))){const path=join(root,item.name);if(item.isDirectory())result.push(...await files(path));else if(item.isFile())result.push(path);}return result;}
export function parseXml(text){
 if(/<!DOCTYPE|<!ENTITY/i.test(text))throw new Error('External/custom XML entities are not supported');
 return new DOMParser({onError:(level,message)=>{if(level!=='warning')throw new Error('Invalid XML: '+message);}}).parseFromString(text,'application/xml');
}
export function xml(root){return '<?xml version="1.0" encoding="UTF-8"?>\n'+new XMLSerializer().serializeToString(root.nodeType===9?root.documentElement:root);}
export function document(name,attributes={}){const doc=parseXml('<'+name+'/>');for(const [key,value] of Object.entries(attributes))doc.documentElement.setAttribute(key,String(value));return doc;}
export function children(node,name){return Array.from(node.childNodes??[]).filter(n=>n.nodeType===1&&(!name||n.tagName===name));}
export const child=(node,name)=>children(node,name)[0];
export function append(node,name,attributes={},text){const doc=node.ownerDocument??node;const element=doc.createElement(name);for(const [key,value] of Object.entries(attributes))element.setAttribute(key,String(value));if(text!==undefined)element.appendChild(doc.createTextNode(text));node.appendChild(element);return element;}
export function manifest(text){const values={};for(const line of text.replace(/\r\n/g,'\n').replace(/\n /g,'').split('\n')){const split=line.indexOf(': ');if(split!==-1)values[line.slice(0,split)]=line.slice(split+2);}return values;}
export function renderManifest(values){const lines=[];for(const [key,value] of Object.entries(values)){let bytes=Buffer.from(key+': '+value),first=true;while(bytes.length){let cut=Math.min(first?70:69,bytes.length);while(cut<bytes.length&&(bytes[cut]&0xc0)===0x80)cut--;lines.push((first?'':' ')+bytes.subarray(0,cut).toString());bytes=bytes.subarray(cut);first=false;}}return lines.join('\r\n')+'\r\n\r\n';}
export function versionKey(value){const match=/^(\d+)\.(\d+)\.(\d+)(?:\.(.*))?$/.exec(value);if(!match)throw new Error('Invalid OSGi version: '+value);return [...match.slice(1,4).map(BigInt),match[4]??''];}
export function compareVersions(a,b){const left=versionKey(a),right=versionKey(b);for(let i=0;i<4;i++)if(left[i]!==right[i])return left[i]<right[i]?-1:1;return 0;}
export function version(value,qualifier){versionKey(value);return value.split('.').slice(0,3).join('.')+'.'+qualifier;}
export function qualifier(){return new Date().toISOString().replace(/\D/g,'')+'000';}
export function validateQualifier(value){if(!/^\d{14,20}(?:_[A-Za-z0-9_-]+)?$/.test(value))throw new Error('Qualifier requires a UTC timestamp and optional run/attempt suffix');const prefix=value.slice(0,14),date=new Date(`${prefix.slice(0,4)}-${prefix.slice(4,6)}-${prefix.slice(6,8)}T${prefix.slice(8,10)}:${prefix.slice(10,12)}:${prefix.slice(12,14)}Z`);if(!Number.isFinite(+date)||date.toISOString().replace(/\D/g,'').slice(0,14)!==prefix)throw new Error('Invalid UTC qualifier date');}
export function checkNewer(name,proposed,published){if(published[name]&&compareVersions(proposed,published[name])<=0)throw new Error(`${name}: proposed ${proposed} must be newer than published ${published[name]}`);}
export const sameSet=(a,b)=>a.size===b.size&&[...a].every(v=>b.has(v));
export async function jsonFile(path,value){await writeFile(path,JSON.stringify(value,null,2)+'\n');}
