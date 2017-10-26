def colorStage() {
    node {
        echo "This is the end!!"
    }
}
def prepareComposeEnvFileFromTemplate(String path, String uid)
{
    sh "cp ${path}/template/.env ${path}"
}
return this;
