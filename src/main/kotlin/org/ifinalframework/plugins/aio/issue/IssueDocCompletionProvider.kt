package org.ifinalframework.plugins.aio.issue

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.tasks.TaskManager
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.resource.AllIcons

/**
 * IssueJavaDocCompletionProvider
 * @author iimik
 */
class IssueDocCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {

        val project = parameters.position.project
        val taskManager = TaskManager.getManager(project)
        val tasks = taskManager.cachedIssues
        tasks?.forEach { issue ->

            var icon = issue.icon
            var tagName = "issue"
            if (issue.repository?.javaClass?.name?.contains("jira", ignoreCase = true) ?: false) {
                tagName = "jira"
                icon = AllIcons.Issues.JIRA
            }
            result.addElement(
                LookupElementBuilder.create("$tagName ${issue.number} ${issue.summary}")
                    .withIcon(icon)
                    .withCaseSensitivity(false)
            )
        }
    }
}