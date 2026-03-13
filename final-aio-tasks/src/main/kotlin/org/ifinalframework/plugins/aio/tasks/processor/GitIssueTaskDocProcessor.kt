package org.ifinalframework.plugins.aio.tasks.processor

import com.intellij.openapi.project.Project
import com.intellij.tasks.Task
import org.ifinalframework.plugins.aio.common.util.getService
import org.ifinalframework.plugins.aio.git.GitService
import org.ifinalframework.plugins.aio.tasks.*

/**
 * GitIssueTaskDocProcessor
 *
 * @author iimik
 */
class GitIssueTaskDocProcessor : TaskDocProcessor {
    private val gitIssueUrlFormatter: GitIssueUrlFormatter = GitVendorIssueUrlFormatter()
    override fun buildTaskDoc(task: Task): TaskDoc? {
        val icon = task.repository?.repositoryType?.icon ?: TasksIcons.ISSUE
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