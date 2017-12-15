import groovy.json.*
echo "This is to check polling"
userId = currentBuild.getRawBuild().getCauses()[0].getUserId()
stime = new Date(currentBuild.startTimeInMillis).format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//build = job.getBuildByNumber(env.BUILD_ID as int)
//userId = build.getCause(Cause.UserIdCause).getUserId()
@NonCPS
def getChangeString() {
    MAX_MSG_LEN = 100
    def changeString = ""

    echo "Gathering SCM changes"
    def changeLogSets = currentBuild.rawBuild.changeSets
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            truncated_msg = entry.msg.take(MAX_MSG_LEN)
            changeString += " - ${truncated_msg} [${entry.author}]\n"
        }
    }

    if (!changeString) {
        changeString = " - No new changes"
    }
    return changeString
}
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

release_version = params.release_version
if (release_version || release_version == ''){
  if (!(release_version ==~ /^\d+\.\d+\.\d+$/)){
    echo "Not a valid version number. Please enter one that follows the"\
         +" semantic versioning pattern '{major}.{minor}.{patch}'"
    currentBuild.result = 'FAILURE'
    return
  }
  echo "release_version is ${release_version}"
}
if (env.release == 'true'){
  echo "this is a release build"
}
if (params.skip_tests)
{
    commands = ' '
    echo commands
}
else
{
    commands = ' && npm run grunt test && cp -r /tmp/workspace/target/reports /testresults'
    echo commands
}
if (env.JOB_NAME == 'test-pipeline') {
  echo "This is the test-pipeline job"
}
def branch = 'master'
build_image='docker-registry.releng.demandware.net/commerce_ui/ui-build-base:0.5.4'
echo "Uploaded: http://nexusmaster.lab.demandware.net/content/repositories/snapshots/dw/ui/ecom.csc/923ab4b-SNAPSHOT/ecom.csc-923ab4b-20171117.131148-1-bin.tgz (2195 KB at 15560.6 KB/sec)"

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
  dir('scripts'){
    sh "pwd"
    ecom_commit = sh returnStdout: true, script: "git rev-parse HEAD"
    ecom_commit = ecom_commit.trim()
    echo ecom_commit
  }
  sh "pwd"
    myModule = load 'scripts/workflow/test.groovy'
    stage('Build') {
        sh "mkdir -p archive/ocapirest"
        testMap = [:]
        def testLists = testSuites.collate( testSuites.size().intdiv(testRunNum))
        if (testLists.size() > testRunNum){
          testLists[testRunNum-1] += (testLists[testRunNum])
          testLists.remove(testLists[testRunNum])
        }
        for ( int i = 0; i < testLists.size(); i++) {
          def index = i
          sh "echo ${testLists[index]} >> archive/ocapirest/ocapiTestSet${index+1}.txt || true"
          testMap["ocapiTests${i+1}"]= {
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
        currentBuild.result = 'SUCCESS'
        myModule.prepareComposeEnvFileFromTemplate('scripts/compose/ocapi', 'test')
          def data = [
            buildinfo:[
              buildNumber: "${env.BUILD_NUMBER}",
              User: "${userId}",
              buildResult: "${currentBuild.result}",
              buildId: "${env.BUILD_ID}",
              timeStamp: "${stime}",
              changeSet: getChangeString()
            ]
          ]
        def jsonOut = readJSON text: groovy.json.JsonOutput.toJson(data)
        writeJSON(file: 'buildInfo.json', json: jsonOut, pretty: 2)
        //myModule.launchEcomContainers(testMap)
    }
    stage('Test') {
        echo 'Building....'
    }
    stage('Deploy') {
        echo 'Deploying....'
        def result=build job: 'downstream'
        //UI_BM_VERSION = result.getDescription().substring(result.getDescription().indexOf(":") + 1)
        UI_BM_VERSION = result.getDescription().split("\\n")[0]
        echo "This is UI_BM_VERSION: ${UI_BM_VERSION}"
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
//currentBuild.setDescription("This is ecom version (.*)", "This is a test project")
myModule.colorStage()
def uploadRegex = "Uploaded:(.*)/ecom\\.csc/(.*)/ecom\\.csc-(.*)\\.tgz.*"
def matcher = manager.getLogMatcher(uploadRegex)
if(matcher?.matches()) {
  myVar = matcher.group(2)
  myVer = matcher.group(3)
  currentBuild.setDescription(myVar + "\n" + myVer + "\n" + "Branch:" + branch)
}

