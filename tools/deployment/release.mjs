import {readFile,writeFile,mkdir,readdir} from 'node:fs/promises';
import {join,resolve,dirname,basename,relative,delimiter} from 'node:path';
import {fileURLToPath} from 'node:url';
import {parseArgs} from 'node:util';
import {modules,repo,tools,exists,files,uri,run,hashFile,hashBytes,manifest,renderManifest,version,compareVersions,qualifier,validateQualifier,checkNewer,parseXml,xml,document,children,append,sameSet,jsonFile,isMain,cli} from './common.mjs';
import {readZip,writeZip,visitZip} from './zip.mjs';
const configs=new Map(modules.map(item=>[item.folder,item]));
const jarPaths=async folder=>(await files(folder)).filter(p=>dirname(p)===folder&&p.endsWith('.jar')&&!p.endsWith('-sources.jar'));
export async function publishedVersions(folder){const root=parseXml((await readZip(join(folder,'content.jar'),'content.xml')).toString()).documentElement;const result={};for(const unit of children(children(root,'units')[0],'unit')){const name=unit.getAttribute('id'),value=unit.getAttribute('version');if(!result[name]||compareVersions(value,result[name])>0)result[name]=value;}return result;}
export async function verifyPrepared(folder,libraries){
 const expected=new Set(libraries.map(p=>'lib/'+basename(p)));
 const root=parseXml(await readFile(join(folder,'.classpath'),'utf8')).documentElement;
 const actual=new Set(children(root,'classpathentry').filter(e=>e.getAttribute('kind')==='lib').map(e=>e.getAttribute('path')));
 if(!sameSet(actual,expected))throw new Error('Generated .classpath does not match resolved libraries: '+folder);
 const prepared=manifest(await readFile(join(folder,'META-INF/MANIFEST.MF'),'utf8'));
 if(!sameSet(new Set(prepared['Bundle-ClassPath']?.split(',')),new Set(['.',...expected])))throw new Error('Prepared Bundle-ClassPath differs: '+folder);
 const properties=await readFile(join(folder,'build.properties'),'utf8');if(properties.includes('LIB_JARS')||[...expected].some(p=>!properties.includes(p)))throw new Error('Prepared build.properties omits libraries: '+folder);
}
function csv(line){const fields=[];let value='',quote=false;for(let i=0;i<line.length;i++){const c=line[i];if(c==='"'){if(quote&&line[i+1]==='"'){value+='"';i++;}else quote=!quote;}else if(c===','&&!quote){fields.push(value);value='';}else value+=c;}fields.push(value);return fields;}
function bundlePath(location,eclipse){if(location.startsWith('file:/'))return fileURLToPath(location);if(/^[a-z][a-z0-9+.-]*:/i.test(location)&&!location.startsWith('file:'))throw new Error('Only file baseline bundles are supported');return resolve(eclipse,decodeURIComponent(location.replace(/^file:/,'')));}
function requiredModules(item,manifests,seen=new Set()){const required=new Set((manifests[item.folder]['Require-Bundle']??'').split(/,(?=(?:[^"]*"[^"]*")*[^"]*$)/).map(s=>s.split(';')[0].trim()));for(const other of modules)if(required.has(other.bundle)&&!seen.has(other.folder)){seen.add(other.folder);requiredModules(other,manifests,seen);}return seen;}
const zipPath=(path,base)=>relative(base,path).split('\\').join('/');
export async function release({eclipse,output,dependenciesFrom,published,buildQualifier=qualifier()}){
 eclipse=resolve(eclipse);output=resolve(output);validateQualifier(buildQualifier);
 if(await exists(output))throw new Error('Output exists; choose a new directory: '+output);
 const entries=[],bundles=new Map();
 for(const line of (await readFile(join(eclipse,'configuration/org.eclipse.equinox.simpleconfigurator/bundles.info'),'utf8')).split(/\r?\n/)){
  if(!line||line.startsWith('#'))continue;const row=csv(line);if(row.length<5)continue;
  const item={id:row[0],version:row[1],path:bundlePath(row[2],eclipse),level:row[3],start:row[4]};
  if(!await exists(item.path))throw new Error('Missing baseline bundle: '+item.path);entries.push(item);
  if(!bundles.has(item.id)||compareVersions(item.version,bundles.get(item.id).version)>0)bundles.set(item.id,item);
 }
 for(const id of ['org.eclipse.osgi','org.eclipse.equinox.simpleconfigurator','org.eclipse.equinox.p2.publisher','org.eclipse.equinox.p2.publisher.eclipse'])if(!bundles.has(id))throw new Error('Baseline missing '+id);
 const installed={},libraries={},manifests={};
 for(const item of modules){
  installed[item.folder]=bundles.get(item.bundle)?.path;
  if(!dependenciesFrom&&item.prepareWithAnt&&!installed[item.folder])throw new Error('Prepared dependencies are required for a bare vendored SDK: '+item.folder);
  manifests[item.folder]=manifest(await readFile(join(repo,item.folder,item.manifest),'utf8'));
  libraries[item.folder]=item.prepareWithAnt?await jarPaths(dependenciesFrom?join(resolve(dependenciesFrom),item.folder,'lib'):join(installed[item.folder],'lib')):[];
  if(item.prepareWithAnt&&!libraries[item.folder].length)throw new Error('No embedded libraries for '+item.folder);
  if(item.prepareWithAnt&&dependenciesFrom)await verifyPrepared(join(resolve(dependenciesFrom),item.folder),libraries[item.folder]);
 }
 const prior=published?await publishedVersions(resolve(published)):{};
 for(const item of modules){checkNewer(item.bundle,version(manifests[item.folder]['Bundle-Version'],buildQualifier),prior);checkNewer(item.feature+'.feature.group',version(item.featureVersion,buildQualifier),prior);}
 const logs=join(output,'logs'),stage=join(output,'stage');await mkdir(logs,{recursive:true});await mkdir(join(stage,'plugins'),{recursive:true});await mkdir(join(stage,'features'));
 const platform=entries.filter(e=>!e.id.startsWith('devrock.eclipse.'));
 const report={qualifier:buildQualifier,eclipse,modules:{},publishedVersions:prior,dependencySource:dependenciesFrom?resolve(dependenciesFrom):'installed bundles',note:'Source/p2 smoke build; no IDE workspace behavior test and no public upload.'};
 const compiled={};
 for(const item of modules){
  console.log('Compile and package '+item.folder);const project=join(repo,item.folder),classes=join(output,'classes',item.folder);await mkdir(classes,{recursive:true});
  const sources=(await files(join(project,'src'))).filter(p=>p.endsWith('.java'));
  const required=requiredModules(item,manifests);for(const dependency of required)if(!compiled[dependency])throw new Error('Build order must place '+dependency+' before '+item.folder);
  const cp=[...[...required].map(name=>compiled[name]),...[...required].flatMap(name=>libraries[name]),...platform.map(e=>e.path),...libraries[item.folder]];
  const args=['--release','17','-encoding','UTF-8','-proc:none','-classpath',cp.join(delimiter),'-d',classes,...sources];
  const argfile=join(output,item.folder+'.javac.args');await writeFile(argfile,args.map(x=>'"'+x.replace(/\\/g,'\\\\').replace(/"/g,'\\"')+'"').join('\n'));
  await run('javac',['@'+argfile],join(logs,item.folder+'-compile.log'));compiled[item.folder]=classes;
  const values={...manifests[item.folder]};if(values['Bundle-SymbolicName'].split(';')[0]!==item.bundle)throw new Error('Unexpected bundle identity');
  const bundleVersion=version(values['Bundle-Version'],buildQualifier);values['Bundle-Version']=bundleVersion;values['Bundle-RequiredExecutionEnvironment']='JavaSE-17';values['Bundle-ClassPath']=['.',...libraries[item.folder].map(p=>'lib/'+basename(p))].join(',');
  const content=[{name:'META-INF/MANIFEST.MF',bytes:renderManifest(values)}],classFiles=await files(classes);
  for(const file of classFiles)content.push({name:zipPath(file,classes),file});
  for(const file of await files(join(project,'src')))if(!/\.(java|class)$/.test(file))content.push({name:zipPath(file,join(project,'src')),file});
  content.push({name:'plugin.xml',file:join(project,'plugin.xml')});for(const file of await files(join(project,'icons')))content.push({name:zipPath(file,project),file});
  for(const file of libraries[item.folder])content.push({name:'lib/'+basename(file),file,compress:false});
  for(const name of ['LICENSE','NOTICE'])if(await exists(join(repo,name)))content.push({name:'META-INF/'+name,file:join(repo,name)});
  const artifact=join(stage,'plugins',item.bundle+'_'+bundleVersion+'.jar');await writeZip(artifact,content);
  const localTemplate=join(tools,'p2/features',item.feature,'feature.xml');
  const candidates=await exists(localTemplate)?[localTemplate]:[];for(const base of [join(eclipse,'features'),...(installed[item.folder]?[join(dirname(dirname(installed[item.folder])),'features')]:[])])if(await exists(base))for(const name of await readdir(base))if(name.startsWith(item.feature+'_')&&await exists(join(base,name,'feature.xml')))candidates.push(join(base,name,'feature.xml'));
  if(!candidates.length)throw new Error('Missing feature template: '+item.feature);
  const feature=parseXml(await readFile(await exists(localTemplate)?localTemplate:[...new Set(candidates)].sort().at(-1),'utf8')),root=feature.documentElement,featureVersion=version(item.featureVersion,buildQualifier);root.setAttribute('version',featureVersion);
  for(const plugin of children(root,'plugin')){if(plugin.getAttribute('id')!==item.bundle)throw new Error('Feature has additional plugin: '+item.feature);plugin.setAttribute('version',bundleVersion);plugin.setAttribute('unpack','true');}
  await writeZip(join(stage,'features',item.feature+'_'+featureVersion+'.jar'),[{name:'feature.xml',bytes:xml(feature)}]);
  const expectedClasses=new Map();for(const file of classFiles.filter(p=>p.endsWith('.class')))expectedClasses.set(zipPath(file,classes),await hashFile(file));
  const expectedLibraries=new Set(libraries[item.folder].map(p=>'lib/'+basename(p))),actualLibraries=new Set();
  await visitZip(artifact,async(entry,read)=>{if(entry.fileName.startsWith('lib/')&&entry.fileName.endsWith('.jar'))actualLibraries.add(entry.fileName);if(entry.fileName.endsWith('.class')){if(hashBytes(await read())!==expectedClasses.get(entry.fileName))throw new Error('Bundle contains stale bytecode: '+entry.fileName);expectedClasses.delete(entry.fileName);}});
  if(expectedClasses.size||!sameSet(expectedLibraries,actualLibraries))throw new Error('Bundle classes or libraries differ from compiler output: '+item.folder);
  const hashes={};for(const file of libraries[item.folder])hashes[basename(file)]=await hashFile(file);
  report.modules[item.folder]={bundle:item.bundle,version:bundleVersion,feature:item.feature,featureVersion,sources:sources.length,jarSha256:await hashFile(artifact),libraries:hashes};
 }
 const config=join(output,'configuration');await mkdir(config);
 const settings={'osgi.bundles':'reference:'+uri(bundles.get('org.eclipse.equinox.simpleconfigurator').path)+'@1:start','osgi.framework':uri(bundles.get('org.eclipse.osgi').path),'osgi.bundles.defaultStartLevel':'4','org.eclipse.equinox.simpleconfigurator.configUrl':uri(join(config,'bundles.info')),'eclipse.p2.data.area':uri(join(output,'p2'))};
 if(bundles.has('org.eclipse.osgi.compatibility.state'))settings['osgi.framework.extensions']='reference:'+uri(bundles.get('org.eclipse.osgi.compatibility.state').path);
 await writeFile(join(config,'config.ini'),Object.entries(settings).map(([k,v])=>k+'='+v).join('\n')+'\n');
 await writeFile(join(config,'bundles.info'),'#version=1\n'+platform.map(e=>[e.id,e.version,uri(e.path),e.level,e.id==='org.eclipse.equinox.p2.reconciler.dropins'?'false':e.start].join(',')).join('\n')+'\n');
 let launchers=(await files(join(eclipse,'plugins'))).filter(p=>dirname(p)===join(eclipse,'plugins')&&basename(p).startsWith('org.eclipse.equinox.launcher_')&&p.endsWith('.jar'));
 if(!launchers.length)launchers=(await files(dirname(bundles.get('org.eclipse.osgi').path))).filter(p=>dirname(p)===dirname(bundles.get('org.eclipse.osgi').path)&&basename(p).startsWith('org.eclipse.equinox.launcher_')&&p.endsWith('.jar'));
 if(launchers.length!==1)throw new Error('Expected exactly one Equinox launcher');
 report.publisher={launcher:launchers[0],configuration:config,workspace:join(output,'workspace')};
 const common=publisherCommand(report),site=join(output,'site');await mkdir(site);console.log('Publish local p2 metadata');
 await run('java',[...common,'-application','org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher','-source',stage,'-metadataRepository',uri(site),'-artifactRepository',uri(site),'-compress','-publishArtifacts'],join(logs,'p2-publish.log'));
 const category=document('site'),categoryRoot=category.documentElement;append(categoryRoot,'category-def',{name:'devrock',label:'Devrock development tools'});
 for(const item of Object.values(report.modules)){const feature=append(categoryRoot,'feature',{id:item.feature,version:item.featureVersion,url:'features/'+item.feature+'_'+item.featureVersion+'.jar'});append(feature,'category',{name:'devrock'});}
 const categoryFile=join(output,'category.xml');await writeFile(categoryFile,xml(category));await run('java',[...common,'-application','org.eclipse.equinox.p2.publisher.CategoryPublisher','-metadataRepository',uri(site),'-categoryDefinition',uri(categoryFile),'-categoryQualifier',buildQualifier],join(logs,'p2-category.log'));
 const baseline=join(output,'baseline');await mkdir(baseline);console.log('Validate installability using headless p2 director');
 await run('java',[...common,'-application','org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher','-bundles',platform.map(e=>e.path).join(','),'-metadataRepository',uri(baseline),'-artifactRepository',uri(baseline),'-compress','-publishArtifacts'],join(logs,'baseline-publish.log'));
 const roots=Object.values(report.modules).map(item=>item.feature+'.feature.group/'+item.featureVersion).join(',');
 await run('java',[...common,'-application','org.eclipse.equinox.p2.director','-repository',uri(site)+','+uri(baseline),'-installIU',roots,'-destination',join(output,'smoke-install'),'-profile','DevrockReleaseSmoke','-addJREIU','-p2.os','linux','-p2.ws','gtk','-p2.arch','x86_64'],join(logs,'p2-install.log'));
 report.site=uri(site);report.verification='p2 director actual installation into an empty isolated profile from local repositories';await jsonFile(join(output,'build-report.json'),report);console.log('Built and verified: '+report.site);return report;
}
export function publisherCommand(report){const p=report.publisher;return ['-jar',p.launcher,'-nosplash','-consolelog','-configuration',p.configuration,'-data',p.workspace];}
if(isMain(import.meta.url))cli(async()=>{const {values}=parseArgs({options:{eclipse:{type:'string'},output:{type:'string'},'dependencies-from':{type:'string'},published:{type:'string'},qualifier:{type:'string'}}});if(!values.eclipse||!values.output)throw new Error('--eclipse and --output are required');await release({eclipse:values.eclipse,output:values.output,dependenciesFrom:values['dependencies-from'],published:values.published,buildQualifier:values.qualifier});});
