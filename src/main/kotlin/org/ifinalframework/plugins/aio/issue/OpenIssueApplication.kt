package org.ifinalframework.plugins.aio.issue;

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.application.ElementHandler
import org.ifinalframework.plugins.aio.application.annotation.ElementApplication
import org.ifinalframework.plugins.aio.git.DefaultGitService
import org.ifinalframework.plugins.aio.psi.service.DocService


/**
 * OpenIssueApplication
 *
 * @author iimik
 * @since 0.0.1
 **/
@ElementApplication(
    value = [
        DefaultIssueService::class,
        DocService::class,
        DefaultGitService::class,
        GitIssueOpener::class,
        JiraIssueOpener::class
    ]
)
class OpenIssueApplication(
    private val issueService: IssueService,
    private val issueOpeners: List<IssueOpener>
) : ElementHandler {

    override fun handle(element: PsiElement) {
        val issue = issueService.getIssue(element) ?: return
        issueOpeners.forEach { it.open(issue) }
    }
}