import jenkins.model.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition

def jenkins = Jenkins.get()

if (jenkins.getItem('deploy-web') == null) {
    // /workspace/Jenkinsfile 내용을 읽어서 파이프라인 등록
    def jenkinsfileContent = new File('/workspace/Jenkinsfile').text

    def job = jenkins.createProject(WorkflowJob.class, 'deploy-web')
    job.setDefinition(new CpsFlowDefinition(jenkinsfileContent, true))
    jenkins.save()

    println "[init] Created 'deploy-web' pipeline job from /workspace/Jenkinsfile"
}
