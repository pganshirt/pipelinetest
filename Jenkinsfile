

def initParams () {
  script_debugger_tests_branch = 'myTestBranch'
  var = 'runTest'
  myModule = null
  mypwd =  '/build/workspace'
  ocapi_jvms = params.OCAPI_JVMS ?: '1'
  runTest = params.runTest
  user_branch = env.TEST_BRANCH ?:
              params.TEST_BRANCH ?:
              'master'
}
bid = "${env.JOB_NAME.replace('-','')}${env.BUILD_NUMBER}"
uid = "${bid}ocapirest"
jvmIndex = ocapi_jvms.toInteger()
exec_num = ocapi_jvms.toInteger()-1
if (params.build_bypass_image_version) {
  echo "build_bypass_image_version is version ${build_bypass_image_version}"
}
initParams()
mylist = (0..exec_num).collect { 
          "-v ${mypwd}/test.groovyBuild.properties:/testrunner/J${it}/test.properties"
          }.join(" ")
echo "${mylist}"
echo "This is runTest ${runTest}.  This is params.runTest ${params.runTest}"
if (runTest) {
    echo "We should run the tests"
}
if (! params.BRANCH) {
  error('Parameter \'BUILD\' must be set')
}
if (params.(var.toString())) {
  myInternalFunction()
}
if (binding.hasVariable('script_debugger_tests_branch')){
    echo "script_debugger_tests_branch is legit"
}
        
scriptDebuggerTestsBranch=env.script_debugger_tests_branch ?: 
       binding.hasVariable('script_debugger_tests_branch') ? 
       script_debugger_tests_branch : 'master'
echo "${scriptDebuggerTestsBranch}"
for (String item : params) {
  echo "${item}"
}

echo "user_branch is ${user_branch}"
node {
  
    checkout([$class: 'GitSCM', branches: [[name: '*/master']], 
              doGenerateSubmoduleConfigurations: false, extensions: 
              [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'scripts']],
              submoduleCfg: [], userRemoteConfigs: 
              [[url: 'https://github.com/pganshirt/pipelinetest.git']]])
    myModule = load 'scripts/workflow/test.groovy'
    stage('Build') {
        for (jvm in 1..jvmIndex) {
            muid = jvm == 1 ? uid : "${uid}${jvm}"
            sh "echo sh cd pipeline.scripts/ocapirest && docker-compose -f "+
               "../ecom-base-compose.yml -f docker-compose.yml -p ${muid} up -d"
        }
    }
    stage('Test') {
        echo 'Building....'
    }
    stage('Deploy') {
        echo 'Deploying....'
    }
}
def myInternalFunction () {
  echo "myInternalFunction has been called"
}

myModule.colorStage()
