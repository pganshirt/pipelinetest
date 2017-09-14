
def test = null
if (! params.BRANCH) {
  error('Parameter \'BUILD\' must be set')
}

for (String item : params) {
  echo "${item}"
}

user_branch = params.BRANCH ?: 
              'master'
node {
  
    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'scripts']], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/pganshirt/pipelinetest.git']]])
    test = load 'scripts/workflow/test.groovy'
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

test.colorStage()
