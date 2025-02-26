package org.ifinalframework.plugins.aio.issue

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.common.util.getService
import org.ifinalframework.plugins.aio.git.GitService
import org.ifinalframework.plugins.aio.service.BrowserService
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
    private val project: Project,
    /**
     * Git Service
     */
    private val gitService: GitService,
    /**
     * Git issue url formatter
     */
    private val gitIssueUrlFormatter: GitIssueUrlFormatter,
) : IssueOpener {


    @Autowired
    constructor(project: Project, gitService: GitService) : this(project, gitService, GitVendorIssueUrlFormatter())


    private val logger = logger<GitIssueOpener>()
    override fun open(issue: Issue) {

        if (IssueType.ISSUE != issue.type) {
            return
        }

        val remote = gitService.getDefaultRemote()
        val url = gitIssueUrlFormatter.format(remote, issue)
        logger.info("Opening git issue: $url")
        project.getService<BrowserService>().open(url)
    }
}