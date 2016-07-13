def value_stream = "experimental"
def owner_name = "waynr"
def project_name = "experimental-repo-1"
def branch_api = new URL("https://api.github.com/repos/${owner_name}/${project_name}/branches")
def branches = new groovy.json.JsonSlurper().parse(branch_api.newReader())

branches.each {
  def branch_name = it.name

  pipelineJob("${value_stream}_${project_name}_workflow") {
    scm {
      git("git@github.com:${owner_name}/${project_name}.git")
    }
    triggers {
      upstream("${value_stream}_experimental-repo-1_workflow_${branch_name}")
      scm("H/5 * * * *")
    }
    definition {
      cpsScm {
        scm {
          git("git@github.com:${owner_name}/${project_name}.git")
        }
        scriptPath("jenkins/pipelines/pipe-1.groovy")
      }
    }
  }
}