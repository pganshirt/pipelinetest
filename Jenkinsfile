
user_branch = params.BRANCH ?: 'master'
node {
    checkout([$class: 'GitSCM', 
              branches: [[name: "${user_branch}"]], 
          doGenerateSubmoduleConfigurations: false, 
          extensions: [[$class: 'CleanCheckout']], 
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
