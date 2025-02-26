package org.ifinalframework.plugins.aio.issue

import com.intellij.openapi.diagnostic.logger
import org.ifinalframework.plugins.aio.git.GitService
import org.ifinalframework.plugins.aio.service.BrowserService
import org.ifinalframework.plugins.aio.service.DefaultBrowserService
import org.springframework.beans.factory.annotation.Autowired
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
    /**
     * Git Service
     */
    private val gitService: GitService,
    /**
     * Git issue url formatter
     */
    private val gitIssueUrlFormatter: GitIssueUrlFormatter,
    /**
     * Browser service
     */
    private val browserService: BrowserService
) : IssueOpener {

    @Autowired
    constructor(gitService: GitService) : this(gitService, GitVendorIssueUrlFormatter(), DefaultBrowserService())

    private val logger = logger<GitIssueOpener>()
    override fun open(issue: Issue) {

        if (IssueType.ISSUE != issue.type) {
            return
        }

        val remote = gitService.getDefaultRemote()
        val url = gitIssueUrlFormatter.format(remote, issue)
        logger.info("Opening git issue: $url")
        browserService.open(url)
    }
}