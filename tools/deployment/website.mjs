import {readFile,writeFile,mkdir,copyFile,stat,rename} from 'node:fs/promises';
import {join,resolve,basename} from 'node:path';
import {parseArgs} from 'node:util';
import {exists,run,uri,hashFile,parseXml,xml,document,children,child,append,compareVersions,jsonFile,isMain,cli,files} from './common.mjs';
import {readZip,writeZip} from './zip.mjs';
import {publisherCommand} from './release.mjs';
const xmlZip=async(path,name)=>parseXml((await readZip(path,name)).toString()).documentElement;
export const unitKeys=root=>new Set(children(child(root,'units'),'unit').map(e=>JSON.stringify([e.getAttribute('id'),e.getAttribute('version')])));
export const artifactKeys=root=>new Set(children(child(root,'artifacts'),'artifact').map(e=>JSON.stringify(['classifier','id','version'].map(a=>e.getAttribute(a)))));
const escape=value=>value.replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
export function website(content){
 const groups=new Map();for(const unit of children(child(content,'units'),'unit')){let id=unit.getAttribute('id');if(!id.endsWith('.feature.group'))continue;id=id.slice(0,-'.feature.group'.length);const properties=Object.fromEntries(children(child(unit,'properties'),'property').map(p=>[p.getAttribute('name'),p.getAttribute('value')]));if(!groups.has(id))groups.set(id,[]);groups.get(id).push({version:unit.getAttribute('version'),label:properties['org.eclipse.equinox.p2.name']??id});}
 const site=document('site'),root=site.documentElement;append(append(root,'category-def',{name:'devrock_tooling',label:'Devrock tooling'}),'description',{},'Current and archived Devrock Eclipse features');
 const rows=[];let count=0;for(const [id,items] of [...groups].sort(([a],[b])=>a.localeCompare(b,'en')))for(const item of items.sort((a,b)=>compareVersions(b.version,a.version))){const filename='features/'+id+'_'+item.version+'.jar';append(append(root,'feature',{id,version:item.version,url:filename}),'category',{name:'devrock_tooling'});rows.push(`<tr><td>${escape(item.label)}</td><td><code>${escape(id)}</code></td><td><a href="${escape(filename)}">${escape(item.version)}</a></td></tr>`);count++;}
 const html='<!doctype html><html lang="en"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Devrock Eclipse updates</title><style>body{font:15px/1.6 system-ui,sans-serif;color:#263b31;background:white;max-width:1350px;margin:35px auto;padding:0 24px}h1,a{color:#236846}h1{border-bottom:3px solid #d8b44b}table{border-collapse:collapse}th,td{text-align:left;padding:5px 14px;border-bottom:1px solid #e3e9e4}th{color:#68766d}code{font-size:12px}</style><h1>Devrock Eclipse updates</h1><p>Use this directory URL as your Eclipse software site. Current and archived features are listed below.</p><table><thead><tr><th>Feature</th><th>Identifier</th><th>Version</th></tr></thead><tbody>'+rows.join('')+'</tbody></table></html>';
 return {site:xml(site),html,count};
}
export async function prepareWebsite({build,previous,output}){
 build=resolve(build);previous=resolve(previous);output=resolve(output);if(await exists(output))throw new Error('Output exists; candidates are not overwritten');
 const report=JSON.parse(await readFile(join(build,'build-report.json'),'utf8'));
 const oldContent=await xmlZip(join(previous,'content.jar'),'content.xml'),oldArtifacts=await xmlZip(join(previous,'artifacts.jar'),'artifacts.xml'),newContent=await xmlZip(join(build,'site/content.jar'),'content.xml'),newArtifacts=await xmlZip(join(build,'site/artifacts.jar'),'artifacts.xml');
 const oldKeys=artifactKeys(oldArtifacts),newKeys=artifactKeys(newArtifacts);if([...newKeys].some(key=>oldKeys.has(key)))throw new Error('Existing immutable artifact versions would be reused');
 await mkdir(output,{recursive:true});for(const name of ['content.jar','artifacts.jar'])await copyFile(join(previous,name),join(output,name));
 await run('java',[...publisherCommand(report),'-application','org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher','-source',join(build,'stage'),'-metadataRepository',uri(output),'-artifactRepository',uri(output),'-append','-compress','-publishArtifacts'],join(output,'merge.log'));
 const merged=await xmlZip(join(output,'content.jar'),'content.xml'),mergedArtifacts=await xmlZip(join(output,'artifacts.jar'),'artifacts.xml');
 if([...unitKeys(oldContent)].some(key=>!unitKeys(merged).has(key)))throw new Error('Merge lost existing unit versions');
 if([...oldKeys].some(key=>!artifactKeys(mergedArtifacts).has(key)))throw new Error('Merge lost existing artifact versions');
 const present=unitKeys(merged),units=child(merged,'units');for(const unit of children(child(newContent,'units'),'unit')){const key=JSON.stringify([unit.getAttribute('id'),unit.getAttribute('version')]);if(!present.has(key)){units.appendChild(merged.ownerDocument.importNode(unit,true));present.add(key);}}
 units.setAttribute('size',String(children(units).length));const temporary=join(output,'content.next');await writeZip(temporary,[{name:'content.xml',bytes:xml(merged)}]);await rename(temporary,join(output,'content.jar'));
 const page=website(merged);await writeFile(join(output,'site.xml'),page.site);await writeFile(join(output,'index.html'),page.html);
 const descriptor=async(path,name)=>({path:name,sha256:await hashFile(path),size:(await stat(path)).size});
 const payloads=[];for(const folder of ['plugins','features'])for(const path of await files(join(output,folder)))if(path.endsWith('.jar'))payloads.push(await descriptor(path,folder+'/'+basename(path)));
 const metadata=[];for(const name of ['artifacts.jar','content.jar','site.xml','index.html'])metadata.push(await descriptor(join(output,name),name));
 const retained=[];for(const key of [...oldKeys].sort()){const [classifier,id,number]=JSON.parse(key),folder={'osgi.bundle':'plugins','org.eclipse.update.feature':'features'}[classifier];if(!folder)throw new Error('Unsupported legacy classifier: '+classifier);retained.push(folder+'/'+id+'_'+number+'.jar');}
 const hashes={};for(const name of ['content.jar','artifacts.jar'])hashes[name]=await hashFile(join(previous,name));
 const plan={qualifier:report.qualifier,retainedPayloads:retained,previousMetadata:hashes,payloadsFirst:payloads,metadataLast:metadata,retainedArtifactVersions:oldKeys.size,addedArtifactVersions:newKeys.size,featureVersionsInHtmlAndSiteXml:page.count,rules:['Never delete or overwrite existing versioned payloads.','Check previousMetadata before publishing.','Verify all new payloads before metadata activation.','Activate artifacts.jar before content.jar, then site.xml and index.html.']};
 await jsonFile(join(output,'publication-plan.json'),plan);console.log(`Cumulative website: ${oldKeys.size} retained + ${newKeys.size} added artifact versions; ${page.count} feature versions in HTML/site.xml`);return plan;
}
if(isMain(import.meta.url))cli(async()=>{const {values}=parseArgs({options:{build:{type:'string'},previous:{type:'string'},output:{type:'string'}}});if(!values.build||!values.previous||!values.output)throw new Error('--build, --previous and --output are required');await prepareWebsite(values);});
