package org.ifinalframework.plugins.aio.tasks.processor

import com.intellij.openapi.project.Project
import com.intellij.tasks.Task
import org.ifinalframework.plugins.aio.common.util.getService
import org.ifinalframework.plugins.aio.git.GitService
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.tasks.GitIssueUrlFormatter
import org.ifinalframework.plugins.aio.tasks.GitVendorIssueUrlFormatter
import org.ifinalframework.plugins.aio.tasks.TaskDoc
import org.ifinalframework.plugins.aio.tasks.TaskDocProcessor

/**
 * GitIssueTaskDocProcessor
 *
 * @author iimik
 */
class GitIssueTaskDocProcessor : TaskDocProcessor {
    private val gitIssueUrlFormatter: GitIssueUrlFormatter = GitVendorIssueUrlFormatter()
    override fun buildTaskDoc(task: Task): TaskDoc? {
        val icon = task.repository?.repositoryType?.icon ?: AllIcons.Task.ISSUE
        return TaskDoc(
            "issue",
            task.number,
            task.summary,
            icon

        )
    }

    override fun buildUrl(project: Project, taskDoc: TaskDoc): String? {
        val remote = project.getService<GitService>().getDefaultRemote()
        return gitIssueUrlFormatter.format(remote, taskDoc)
    }
}