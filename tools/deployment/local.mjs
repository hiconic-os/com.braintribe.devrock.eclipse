#!/usr/bin/env node
import {parseArgs} from 'node:util';
import {resolve,join} from 'node:path';
import {repo,qualifier,isMain,cli} from './common.mjs';
import {build} from './build.mjs';
import {release} from './release.mjs';
import {publishSite} from './site.mjs';
import {vendoredSdk} from './vendor.mjs';
export async function local(options){
 const workspace=resolve(repo,'../..'),runtime=resolve(options.runtime??join(workspace,'runtime/eclipse-local-builds')),root=resolve(options.webRoot??join(workspace,'runtime/eclipse-update-site')),output=join(runtime,qualifier());let site;
 if(options.dependenciesFrom){await release({eclipse:options.eclipse??await vendoredSdk(output),output:join(output,'build'),dependenciesFrom:options.dependenciesFrom,published:options.published});site=join(output,'build/site');}
 else site=join(await build({work:output,baseline:options.eclipse,refresh:!options.baselineDependencies}),'site');
 await publishSite({root,site});console.log('Local update site directory: '+root);console.log('Serve with: node tools/deployment/site.mjs --root '+root+' serve --port 8098');return site;
}
if(isMain(import.meta.url))cli(async()=>{const {values}=parseArgs({options:{eclipse:{type:'string'},runtime:{type:'string'},'web-root':{type:'string'},'dependencies-from':{type:'string'},published:{type:'string'},'baseline-dependencies':{type:'boolean',default:false}}});await local({eclipse:values.eclipse,runtime:values.runtime,webRoot:values['web-root'],dependenciesFrom:values['dependencies-from'],published:values.published,baselineDependencies:values['baseline-dependencies']});});
