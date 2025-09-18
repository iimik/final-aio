package org.ifinalframework.plugins.aio.tasks;

import org.apache.commons.lang3.StringUtils


/**
 * JiraIssueUrlFormatter
 * @issue 12
 * @jira 2868
 * @jira 1 https://iimik.atlassian.net/browse/AIO-1
 * @author iimik
 * @since 0.0.1
 **/
class JiraIssueUrlFormatter : IssueUrlFormatter<IssueProperties.JiraIssueProperties> {
    override fun format(issue: Issue, param: IssueProperties.JiraIssueProperties): String? {
        if (StringUtils.isAnyBlank(param.serverUrl, param.projectCode)) {
            return null
        }

        return "${param.serverUrl}/browse/${param.projectCode}-${issue.code}"

    }
}