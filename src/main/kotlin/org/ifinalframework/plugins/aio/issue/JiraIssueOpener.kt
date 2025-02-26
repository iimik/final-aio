package org.ifinalframework.plugins.aio.issue;

import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.common.util.getService
import org.ifinalframework.plugins.aio.service.BrowserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component


/**
 * JiraIssueOpener
 * @issue 12
 * @jira 1 https://iimik.atlassian.net/browse/AIO-1
 * @author iimik
 * @since 0.0.1
 **/
@Component
@EnableConfigurationProperties(JiraIssueProperties::class)
class JiraIssueOpener(
    private val project: Project,
    private val jiraIssueProperties: JiraIssueProperties,
    private val issueUrlFormatter: IssueUrlFormatter<JiraIssueProperties>,

    ) : IssueOpener {
    @Autowired
    constructor(project: Project, properties: JiraIssueProperties) : this(project, properties, JiraIssueUrlFormatter())

    override fun open(issue: Issue) {
        if (IssueType.JIRA != issue.type) {
            return
        }
        val url = issueUrlFormatter.format(issue, jiraIssueProperties) ?: return
        project.getService<BrowserService>().open(url)
    }
}