package org.ifinalframework.plugins.aio.issue

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.service.BrowserService


/**
 * JiraIssueOpener
 * @issue 12
 * @jira 1 https://iimik.atlassian.net/browse/AIO-1
 * @author iimik
 * @since 0.0.1
 * @see GitIssueOpener
 **/
@Service(Service.Level.PROJECT)
class JiraIssueOpener(
    private val project: Project,
) : IssueOpener {
    private val issueUrlFormatter: IssueUrlFormatter<IssueProperties.JiraIssueProperties> = JiraIssueUrlFormatter()

    override fun open(issue: Issue) {
        if (IssueType.JIRA != issue.type) {
            return
        }
        val issueProperties = project.service<IssueProperties>()
        val url = issueUrlFormatter.format(issue, issueProperties.jiraIssue) ?: return
        service<BrowserService>().open(url)
    }
}