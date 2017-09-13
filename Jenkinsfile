if (! params.BRANCH) {
  error('Parameter \'BUILD\' must be set')
}

for (String item : params) {
  echo item
}

user_branch = params.BRANCH ?: 
              'master'
node {
    checkout([$class: 'GitSCM', 
              branches: [[name: "${user_branch}"]], 
          doGenerateSubmoduleConfigurations: false, 
          extensions: [[$class: 'CleanCheckout', relativeTargetDir: 'scripts']], 
          submoduleCfg: [], 
          userRemoteConfigs: [[url: 'https://github.com/pganshirt/pipelinetest.git']]])
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
