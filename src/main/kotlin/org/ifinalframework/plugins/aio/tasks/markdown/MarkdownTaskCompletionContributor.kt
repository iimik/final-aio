package org.ifinalframework.plugins.aio.tasks.markdown

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.jvm.dfa.analysis.ui.inspection.presentation.PsiElementLineLocator.getStartLine
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.tasks.TaskManager
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.resource.AllIcons

/**
 * Markdown 文档补全
 *
 * 支持以下格式的补全：
 *
 * - [#id](url)
 * - [code-summary](url)
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
                    position.getStartLine()
                    val lineStartOffset = parameters.editor.document.getLineStartOffset(position.getStartLine() - 1)
                    val line = parameters.editor.document.getText(TextRange(lineStartOffset, position.textOffset))
                    val isHeader = line.startsWith("#")

                    if (text.startsWith("@") || text.startsWith("#")) {
                        if (isHeader) {
                            val project = position.project
                            val taskManager = TaskManager.getManager(project)
                            val tasks = taskManager.cachedIssues
                            tasks?.forEach { issue ->
                                var icon = issue.icon
                                var tagName = "issue"
                                if (issue.repository?.javaClass?.name?.contains("jira", ignoreCase = true) ?: false) {
                                    tagName = "jira"
                                    icon = AllIcons.Task.JIRA
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
                        } else {
                            val project = position.project
                            val taskManager = TaskManager.getManager(project)
                            val tasks = taskManager.cachedIssues
                            tasks?.forEach { issue ->
                                var icon = issue.icon
                                var tagName = "issue"
                                if (issue.repository?.javaClass?.name?.contains("jira", ignoreCase = true) ?: false) {
                                    tagName = "jira"
                                    icon = AllIcons.Task.JIRA
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
                                            val content = "[#${issue.id}](${issue.issueUrl})"
                                            document.replaceString(offset, offset + lookupString.length + 1, content)
                                        }
                                )
                            }
                        }
                    }

                }
            })
    }
}