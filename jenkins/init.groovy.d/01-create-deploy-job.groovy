import jenkins.model.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition

def jenkins = Jenkins.get()

// 기동마다 /workspace/Jenkinsfile 내용으로 동기화 (변경사항 자동 반영)
def jenkinsfileContent = new File('/workspace/jenkins/Jenkinsfile').text

def job = jenkins.getItem('deploy-web')
if (job == null) {
    job = jenkins.createProject(WorkflowJob.class, 'deploy-web')
    println "[init] Created 'deploy-web' pipeline job"
} else {
    println "[init] Updated 'deploy-web' pipeline job"
}

job.setDefinition(new CpsFlowDefinition(jenkinsfileContent, true))
jenkins.save()
