#!/usr/bin/env node
/** Maintainer-only import of a verified SDK into Git. Never invoked by normal builds. */
import {readFile,writeFile,mkdir,cp,stat,readdir} from 'node:fs/promises';
import {join,resolve,basename,relative} from 'node:path';
import {fileURLToPath} from 'node:url';
import {parseArgs} from 'node:util';
import {repo,exists,files,hashFile,jsonFile,isMain,cli} from './common.mjs';
export async function importSdk({source,archive,target=join(repo,'tools/vendor/eclipse-4.40')}){
 source=resolve(source);target=resolve(target);if(await exists(target))throw new Error('Vendor target exists; SDK imports are immutable');
 const lock=JSON.parse(await readFile(join(repo,'tools/deployment/baseline.json'),'utf8'));
 if(!archive||await hashFile(archive,'sha512')!==lock.sdkSha512)throw new Error('Supply the original SDK archive with its pinned SHA-512');
 const info=await readFile(join(source,'configuration/org.eclipse.equinox.simpleconfigurator/bundles.info'),'utf8'),bundles=[];
 await mkdir(join(target,'plugins'),{recursive:true});
 for(const line of info.split(/\r?\n/)){
  if(!line||line.startsWith('#'))continue;const [id,version,location,level,start]=line.split(',');
  if(!id||id.startsWith('devrock.')||id.includes('.source')||id.includes('.doc.'))continue;
  let path=location.startsWith('file:/')?fileURLToPath(location):resolve(source,decodeURIComponent(location.replace(/^file:/,'')));
  const name=basename(path);await cp(path,join(target,'plugins',name),{recursive:true});bundles.push({id,version,path:'plugins/'+name,level,start});
 }
 for(const name of await readdir(source))if(/(about|license|notice|epl).*\.html$/i.test(name))await cp(join(source,name),join(target,name));
 // SDK startup/configuration, p2 profiles and personal caches are deliberately not imported.
 const fingerprints=[];let size=0;
 for(const path of await files(target)){const bytes=(await stat(path)).size;size+=bytes;fingerprints.push({path:relative(target,path).split('\\').join('/'),size:bytes,sha256:await hashFile(path)});}
 await jsonFile(join(target,'vendor.json'),{version:'4.40',platform:'linux-gtk-x86_64',sourceUrl:lock.sdkUrl,sourceArchiveSha512:lock.sdkSha512,omitted:'source and documentation bundles; startup and profile state',bundles,files:fingerprints});
 await writeFile(join(target,'README.md'),'# Vendored Eclipse SDK binaries\n\nPinned Eclipse SDK 4.40 Linux x86_64 runtime/build bundles, imported from the official\narchive verified by SHA-512. Source and documentation bundles are omitted. The\nindividual files, bundle identities and SHA-256 values are recorded in vendor.json.\nAll embedded license/about files remain intact. Source URL and original archive\nchecksum are also in vendor.json. This directory is read-only to normal builds.\nA toolchain update is a reviewed repository change, not an automatic download.\n');
 console.log(`Vendored ${bundles.length} SDK bundles, ${(size/1048576).toFixed(1)} MiB. No Git LFS or external bootstrap is required.`);
}
if(isMain(import.meta.url))cli(async()=>{const {values}=parseArgs({options:{source:{type:'string'},archive:{type:'string'},target:{type:'string'}}});if(!values.source||!values.archive)throw new Error('--source and verified --archive are required');await importSdk(values);});
