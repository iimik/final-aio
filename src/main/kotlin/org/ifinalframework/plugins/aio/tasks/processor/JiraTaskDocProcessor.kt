package org.ifinalframework.plugins.aio.tasks.processor

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.tasks.Task
import org.ifinalframework.plugins.aio.service.NotificationService
import org.ifinalframework.plugins.aio.tasks.TaskDoc
import org.ifinalframework.plugins.aio.tasks.TaskDocProcessor
import org.ifinalframework.plugins.aio.tasks.TaskUtils

/**
 * JiraTaskDocProcessor
 *
 * @author iimik
 */
class JiraTaskDocProcessor : TaskDocProcessor {

    override fun buildTaskDoc(task: Task): TaskDoc {
        val icon = task.repository!!.repositoryType.icon
        return TaskDoc(
            "jira",
            task.id,
            task.summary,
            icon
        )
    }

    override fun buildUrl(project: Project, taskDoc: TaskDoc): String? {
        val repository = TaskUtils.getTaskRepository(project, "JIRA")
        if(repository == null){
            service<NotificationService>().warn("您还没有配置JIRA Server，请前往【Settings->Tools->Tasks->Servers】添加")
            return null
        }

        val url = repository.url

        val code = taskDoc.code
        if(code.contains("-")){
            return "$url/browse/$code"
        }else{


            try {
                val issues = repository.getIssues(null, 0, 100, true)
                if (issues.isNotEmpty()) {
                    val projects = issues.map { it.project }.distinct()
                    if (projects.size == 1) {
                        // 仅有一个Project，打开
                        return "$url/browse/${projects[0]}-$code"
                        // TODO 更新
                    } else {
                        // TODO 有多个Project，选择并更新
                    }
                }


                service<NotificationService>().warn("无法推断的Jira项目:$code ${taskDoc.summary}")
                return null
            }catch (ex: Exception){
                // 可能网络不通
                service<NotificationService>().warn("无法访问Jira Server：$url")
                return null
            }
        }

    }
}