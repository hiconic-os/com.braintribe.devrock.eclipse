import yauzl from 'yauzl';
import yazl from 'yazl';
import {createWriteStream} from 'node:fs';
import {pipeline} from 'node:stream/promises';
import {hashBytes} from './common.mjs';
const open=path=>new Promise((ok,bad)=>yauzl.open(path,{lazyEntries:true,autoClose:true},(error,zip)=>error?bad(error):ok(zip)));
export async function visitZip(path,visitor){
 const zip=await open(path),names=new Set();
 return new Promise((ok,bad)=>{let stopped=false;const fail=e=>{if(!stopped){stopped=true;zip.close();bad(e);}};zip.on('error',fail);zip.on('end',()=>{if(!stopped){stopped=true;ok(names);}});zip.on('entry',entry=>{
  if(names.has(entry.fileName)){fail(new Error('Duplicate ZIP entry: '+entry.fileName));return;}names.add(entry.fileName);
  const read=async(max=64*1024*1024)=>{if(entry.uncompressedSize>max)throw new Error('ZIP entry too large: '+entry.fileName);const stream=await new Promise((yes,no)=>zip.openReadStream(entry,(e,s)=>e?no(e):yes(s)));const chunks=[];let size=0;for await(const chunk of stream){size+=chunk.length;if(size>max)throw new Error('ZIP entry exceeds limit');chunks.push(chunk);}return Buffer.concat(chunks);};
  Promise.resolve(visitor(entry,read)).then(()=>{if(!stopped)zip.readEntry();},fail);
 });zip.readEntry();});
}
export async function readZip(path,name){let result;await visitZip(path,async(entry,read)=>{if(entry.fileName===name)result=await read();});if(!result)throw new Error('Missing ZIP member '+name+' in '+path);return result;}
export async function writeZip(path,entries){
 const zip=new yazl.ZipFile(),names=new Set();const done=pipeline(zip.outputStream,createWriteStream(path,{flags:'wx'}));
 try{for(const entry of entries){if(names.has(entry.name))throw new Error('Duplicate ZIP output: '+entry.name);names.add(entry.name);const options={mtime:new Date('1980-01-01T00:00:00Z'),compress:true,compressionLevel:1};if(entry.file)zip.addFile(entry.file,entry.name,options);else zip.addBuffer(Buffer.isBuffer(entry.bytes)?entry.bytes:Buffer.from(entry.bytes),entry.name,options);}zip.end();await done;}catch(error){zip.outputStream.destroy(error);await done.catch(()=>{});throw error;}
}
