import {readFile,writeFile,mkdir} from 'node:fs/promises';
import {join,resolve} from 'node:path';
import {repo,hashFile,uri,exists} from './common.mjs';
export async function vendoredSdk(work){
 const lock=JSON.parse(await readFile(join(repo,'tools/deployment/baseline.json'),'utf8'));
 const root=resolve(repo,lock.vendorDirectory),manifest=join(root,'vendor.json');
 if(await hashFile(manifest)!==lock.vendorManifestSha256)throw new Error('Vendored SDK inventory checksum differs from baseline lock');
 const data=JSON.parse(await readFile(manifest,'utf8'));
 for(const file of data.files){const path=resolve(root,file.path);if(!path.startsWith(root+'/'))throw new Error('Unsafe vendor file path');if(!await exists(path)||await hashFile(path)!==file.sha256)throw new Error('Vendored SDK file checksum differs: '+file.path);}
 const target=join(resolve(work),'sdk'),config=join(target,'configuration/org.eclipse.equinox.simpleconfigurator');await mkdir(config,{recursive:true});
 await writeFile(join(config,'bundles.info'),'#version=1\n'+data.bundles.map(b=>[b.id,b.version,uri(join(root,b.path)),b.level,b.start].join(',')).join('\n')+'\n');
 return target;
}
