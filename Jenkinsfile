script_debugger_tests_branch = 'myTestBranch'
var = 'runTest'
myModule = null
if (! params.BRANCH) {
  error('Parameter \'BUILD\' must be set')
}
if (params.(var.toString())) {
  myInternalFunction()
}
def resolveVar(var) {
  resolvedVar = env.(var.toString()) ?:
      binding.hasVariable(var) ?

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

user_branch = env.BRANCH ?:
              params.BRANCH ?:
              'master'
node {
  
    checkout([$class: 'GitSCM', branches: [[name: '*/master']], 
              doGenerateSubmoduleConfigurations: false, extensions: 
              [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'scripts']],
              submoduleCfg: [], userRemoteConfigs: 
              [[url: 'https://github.com/pganshirt/pipelinetest.git']]])
    myModule = load 'scripts/workflow/test.groovy'
    stage('Build') {
        echo 'Building....'
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
