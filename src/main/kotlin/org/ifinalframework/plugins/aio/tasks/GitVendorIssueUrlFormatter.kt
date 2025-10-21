package org.ifinalframework.plugins.aio.tasks;

import org.ifinalframework.plugins.aio.git.GitRemote
import org.ifinalframework.plugins.aio.git.GitVendor
import org.springframework.stereotype.Component


/**
 * DefaultGitIssueUrlFormatter
 *
 * @author iimik
 * @since 0.0.1
 **/
@Component
class GitVendorIssueUrlFormatter : GitIssueUrlFormatter {

    override fun format(remote: GitRemote, issue: TaskDoc): String {

        val gitVendor = GitVendor.getByHostOrDefault(remote.host)
        val issueFormat = gitVendor.issueFormat

        return issueFormat.replace("\${schema}", remote.schema ?: "https")
            .replace("\${host}", remote.host)
            .replace("\${path}", remote.path)
            .replace("\${issue}", issue.code)
    }
}