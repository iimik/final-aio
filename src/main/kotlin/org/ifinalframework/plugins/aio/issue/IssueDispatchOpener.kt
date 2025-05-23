package org.ifinalframework.plugins.aio.issue

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

/**
 * IssueDispatchOpener
 *
 * @author iimik
 */
class IssueDispatchOpener(val project: Project): IssueOpener {
    override fun open(issue: Issue) {

        when(issue.type){
            IssueType.ISSUE -> {
                project.service<GitIssueOpener>().open(issue)
            }
            IssueType.JIRA -> {
                project.service<JiraIssueOpener>().open(issue)
            }
        }

    }
}