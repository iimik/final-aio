package org.ifinalframework.plugins.aio.issue;

import org.ifinalframework.plugins.aio.service.BrowserService
import org.ifinalframework.plugins.aio.service.DefaultBrowserService
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
    private val jiraIssueProperties: JiraIssueProperties,
    private val browserService: BrowserService,
    private val issueUrlFormatter: IssueUrlFormatter<JiraIssueProperties>,

    ) : IssueOpener {
    @Autowired
    constructor(properties: JiraIssueProperties) : this(properties, DefaultBrowserService(), JiraIssueUrlFormatter())

    override fun open(issue: Issue) {
        if (IssueType.JIRA != issue.type) {
            return
        }
        val url = issueUrlFormatter.format(issue, jiraIssueProperties) ?: return
        browserService.open(url)
    }
}