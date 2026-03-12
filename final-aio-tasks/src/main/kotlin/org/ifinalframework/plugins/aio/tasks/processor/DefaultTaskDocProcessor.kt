package org.ifinalframework.plugins.aio.tasks.processor

import com.intellij.openapi.project.Project
import com.intellij.tasks.Task
import org.ifinalframework.plugins.aio.tasks.TaskDoc
import org.ifinalframework.plugins.aio.tasks.TaskDocProcessor

/**
 * TaskDocProcessors
 *
 * @author iimik
 */
class DefaultTaskDocProcessor : TaskDocProcessor {

    private val processors = mutableMapOf<String, TaskDocProcessor>()


    init {
        processors["JIRA"] = JiraTaskDocProcessor()

        val gitIssueTaskDocProcessor = GitIssueTaskDocProcessor()
        processors["GITHUB"] = gitIssueTaskDocProcessor
        processors["ISSUE"] = gitIssueTaskDocProcessor
    }

    override fun buildTaskDoc(task: Task): TaskDoc? {

        val type = task.repository?.repositoryType?.name ?: return null
        val processor = processors[type.uppercase()] ?: return null
        return processor.buildTaskDoc(task)
    }

    override fun buildUrl(
        project: Project,
        taskDoc: TaskDoc
    ): String? {
        val processor = processors[taskDoc.tag.uppercase()] ?: return null
        return processor.buildUrl(project, taskDoc)
    }
}