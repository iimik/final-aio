package org.ifinalframework.plugins.aio.issue.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PsiJavaPatterns
import com.intellij.psi.JavaDocTokenType
import com.intellij.tasks.TaskManager
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.resource.AllIcons

/**
 * IssueCompletionContributor
 *
 * @author iimik
 * @see [JavaDocCompletionContributor]
 */
class IssueCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            PsiJavaPatterns.psiElement(JavaDocTokenType.DOC_TAG_NAME),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {

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
            })

    }
}