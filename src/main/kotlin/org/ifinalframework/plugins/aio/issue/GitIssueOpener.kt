package org.ifinalframework.plugins.aio.issue

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.common.util.getService
import org.ifinalframework.plugins.aio.git.GitService
import org.ifinalframework.plugins.aio.service.BrowserService
import org.springframework.stereotype.Component


/**
 * 在浏览器中打开Git Issue
 *
 * @issue 11
 * @author iimik
 * @since 0.0.1
 **/
@Component
class GitIssueOpener(
    private val project: Project,
) : IssueOpener {

    private val gitIssueUrlFormatter: GitIssueUrlFormatter = GitVendorIssueUrlFormatter()

    private val logger = logger<GitIssueOpener>()

    override fun open(issue: Issue) {

        if (IssueType.ISSUE != issue.type) {
            return
        }

        val remote = project.getService<GitService>().getDefaultRemote()
        val url = gitIssueUrlFormatter.format(remote, issue)
        logger.info("Opening git issue: $url")
        service<BrowserService>().open(url)
    }
}