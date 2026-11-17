import {test} from 'node:test';
import assert from 'node:assert/strict';
import {probe} from './sftp-probe.mjs';
const hostKey='a'.repeat(64),options={host:'example.org',remoteDir:'/eclipse/test',hostKey};
function fixture(){const files=new Map(),calls=[];return {files,calls,client:{async connect(o){assert.equal(o.hostVerifier(hostKey),true);calls.push('connect');},async exists(){return 'd';},async mkdir(){},async put(b,p){files.set(p,b);calls.push('put');},async rename(a,b){files.set(b,files.get(a));files.delete(a);calls.push('rename');},async get(p){return files.get(p);},async delete(p){files.delete(p);calls.push('delete');},async end(){calls.push('end');}}};}
const credentials={user:'test',password:'test-pass'};
test('SFTP verifies the pinned host key, tests rename and removes the unique file',async()=>{const s=fixture();const r=await probe(options,{...credentials,client:s.client});assert.equal(r.hostKey,hostKey);assert.equal(s.files.size,0);assert.deepEqual(s.calls,['connect','put','rename','delete','end']);});
test('wrong SSH key is rejected before any writes',async()=>{const s=fixture();s.client.connect=async o=>{assert.equal(o.hostVerifier('b'.repeat(64)),false);throw new Error('Host key verification failed');};await assert.rejects(()=>probe(options,{...credentials,client:s.client}),/Host key/);assert.equal(s.files.size,0);assert.deepEqual(s.calls,['end']);});
test('wrong HTTPS mapping removes the uploaded file even if keep was requested',async()=>{const s=fixture();await assert.rejects(()=>probe({...options,keep:true,publicBase:'https://example.org/test/'},{...credentials,client:s.client,fetcher:async()=>new Response('other site')}),/HTTPS verification/);assert.equal(s.files.size,0);});
