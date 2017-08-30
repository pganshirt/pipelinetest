
checkout([$class: 'GitSCM', 
          branches: [[name: '*/master']], 
          doGenerateSubmoduleConfigurations: false, 
          extensions: [[$class: 'CleanCheckout']], 
          submoduleCfg: [], 
          userRemoteConfigs: [[url: 'https://github.com/pganshirt/pipelinetest.git']]])
node {
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
