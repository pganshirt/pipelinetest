

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
def testSuites = ["test_rest","test_rest_batch","test_rest_csc","test_rest_meta",
              "test_rest_data","test_rest_shop","test_rest_shop2","test_rest_oauth",
              "test_rest_webdav","test_rest_integration","test_ecom_server"]
def testRunNum = 2
bid = "${env.JOB_NAME.replace('-','')}${env.BUILD_NUMBER}"
uid = "${bid}ocapirest"

jvmIndex = ocapi_jvms.toInteger()
jvmlist = (0..jvmIndex).toList()
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
        testMap = [:]
        def testLists = testSuites.collate( testSuites.size().intdiv(testRunNum))
        if (testLists.size() > testRunNum){
          testLists[testRunNum-1] += (testLists[testRunNum])
          testLists.remove(testLists[testRunNum])
        }
        for ( int i = 0; i < testLists.size(); i++) {
          def index = i
          testMap["ocapiTests"+(i+1)}]= {
            def tests = testLists[index]
            def testUid = uid+(index+1)
            myInternalFunction(testUid, tests, 'myincludePattern')
          } 
        }
        myOtherFunction(uid,testRunNum)
        //def myClass = testMap[0][1].getClass()
        // echo "${myClass}"
        //echo "${testMap}"
        for (jvm in jvmlist) {
            muid = jvm == 1 ? uid : "${uid}${jvm}"
            sh "echo cd pipeline.scripts/ocapirest && echo docker-compose -f ../ecom-base-compose.yml -f docker-compose.yml -p ${muid} up -d"
        }
        for ( i in 1..testRunNum){
        echo "This is ecom version ${i}"
        }
        parallel testMap      
        myModule.prepareComposeEnvFileFromTemplate('scripts/compose/ocapi', 'test')
        //myModule.launchEcomContainers(testMap)
    }
    stage('Test') {
        echo 'Building....'
    }
    stage('Deploy') {
        echo 'Deploying....'
    }
}
def myInternalFunction (String uid, List testSuite, String includePattern) {
  echo "myInternalFunction has been called with ${uid}, ${testSuite}, ${includePattern}"
}
def myOtherFunction (String uid, int testRunNum) {
    for (int i = 0;i < testRunNum; i++) {
        muid = uid+(i+1)
      echo "printing muid: ${muid}"
    }
}

myModule.colorStage()
