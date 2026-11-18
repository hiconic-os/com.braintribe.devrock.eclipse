import {readFile,writeFile,mkdir} from 'node:fs/promises';
import {execFile} from 'node:child_process';
import {promisify} from 'node:util';
import {join,delimiter,resolve} from 'node:path';
import {parseArgs} from 'node:util';
import {repo,isMain,cli} from './common.mjs';
const execute=promisify(execFile);

export async function testScanners(build){
 build=resolve(build);
 const args=(await readFile(join(build,'devrock.javac.args'),'utf8')).trim().split('\n').map(line=>JSON.parse(line));
 const classpath=[join(build,'classes/devrock'),args[args.indexOf('-classpath')+1]].join(delimiter);
 const output=join(build,'scanner-test');await mkdir(output,{recursive:true});
 const quoted=values=>values.map(value=>JSON.stringify(value)).join('\n');
 const compileArgs=join(output,'compile.args'),runArgs=join(output,'run.args');
 await writeFile(compileArgs,quoted(['--release','17','-proc:none','-classpath',classpath,'-d',output,join(repo,'tools/tests/ScannerDirectoryListingTest.java')]));
 await writeFile(runArgs,quoted(['-classpath',[output,classpath].join(delimiter),'ScannerDirectoryListingTest']));
 const options={timeout:120_000,maxBuffer:1024*1024};
 await execute('javac',['@'+compileArgs],options);
 try {
  const result=await execute('java',['@'+runArgs],options);
  await writeFile(join(build,'logs/scanner-test.log'),result.stdout+result.stderr);
  console.log(result.stdout.trim());
 }catch(error){
  await writeFile(join(build,'logs/scanner-test.log'),(error.stdout??'')+(error.stderr??'')+'\n'+error.message);
  throw new Error('Scanner regression tests failed; inspect '+join(build,'logs/scanner-test.log'));
 }
}
if(isMain(import.meta.url))cli(async()=>{const {values}=parseArgs({options:{build:{type:'string'}}});if(!values.build)throw new Error('--build is required');await testScanners(values.build);});
