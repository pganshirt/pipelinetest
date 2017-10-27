def colorStage() {
    node {
        echo "This is the end!!"
    }
}
def prepareComposeEnvFileFromTemplate(String path, String uid)
{
    sh "cp ${path}/template/.env ${path}"
}
def launchEcomContainers(List containersList)
{
    for (int i = 0;i < containersList.size(); i++){
        echo "Running muid: ${containersList[i][0]} and tests: ${containersList[i][1]}"
    }
}
return this;
