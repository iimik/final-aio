package org.ifinalframework.plugins.aio.tasks

import git4idea.repo.GitRemote
import org.ifinalframework.plugins.aio.git.GitVendor
import java.net.URI


/**
 * DefaultGitIssueUrlFormatter
 *
 * @author iimik
 * @since 0.0.1
 **/
class GitVendorIssueUrlFormatter : GitIssueUrlFormatter {

    override fun format(remote: GitRemote, issue: TaskDoc): String {

        val uri = URI(toWebUrl(remote.firstUrl!!))
        val gitVendor = GitVendor.getByHostOrDefault(uri.host)
        val issueFormat = gitVendor.issueFormat

        return issueFormat.replace($$"${scheme}", uri.scheme ?: "https")
            .replace($$"${host}", uri.host)
            .replace($$"${path}", uri.path)
            .replace($$"${issue}", issue.code)
    }

    private fun toWebUrl(remoteUrl: String): String {
            return when {
                // git@github.com:user/demo.git
                remoteUrl.startsWith("git@") -> {
                    val withoutPrefix = remoteUrl.removePrefix("git@")
                    val (host, path) = withoutPrefix.split(":", limit = 2)
                    "https://$host/${path.removeSuffix(".git")}"
                }

                // https://github.com/user/demo.git
                remoteUrl.startsWith("http://") || remoteUrl.startsWith("https://") -> {
                    remoteUrl.removeSuffix(".git")
                }

                else -> remoteUrl
            }
    }
}