package org.ifinalframework.plugins.aio.tasks.markdown

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.tasks.TaskManager
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.resource.AllIcons

/**
 * MarkdownIssueCompletionContributor
 *
 * @author iimik
 * @since 0.0.19
 */
class MarkdownTaskCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PsiJavaPatterns.psiElement(),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
                    val position = parameters.position
                    val text = position.text
                    if (text.startsWith("#")) {
                        val project = position.project
                        val taskManager = TaskManager.getManager(project)
                        val tasks = taskManager.cachedIssues
                        tasks?.forEach { issue ->
                            var icon = issue.icon
                            var tagName = "issue"
                            if (issue.repository?.javaClass?.name?.contains("jira", ignoreCase = true) ?: false) {
                                tagName = "jira"
                                icon = AllIcons.Issues.JIRA
                            }
                            val summary = issue.summary
                            val lookupString = "${issue.number}-${summary}"
                            result.addElement(
                                LookupElementBuilder.create(issue, lookupString)
                                    .withIcon(icon)
                                    .withCaseSensitivity(false)
                                    .withInsertHandler { context, _ ->
                                        val document = context.document
                                        val offset = position.textOffset
                                        val newSummary = issue.summary
                                            .replace("[", "\\[")
                                            .replace("]", "\\]")
                                        val content = "[${issue.number}-${newSummary}](${issue.issueUrl})"
                                        document.replaceString(offset, offset + lookupString.length + 1, content)
                                    }
                            )
                        }
                    }

                }
            })
    }
}