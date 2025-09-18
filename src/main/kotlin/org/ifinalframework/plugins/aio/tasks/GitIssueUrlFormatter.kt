package org.ifinalframework.plugins.aio.tasks

import org.ifinalframework.plugins.aio.git.GitRemote


/**
 * GitIssueUrlFormatter
 *
 * @author iimik
 * @since 0.0.1
 **/
@FunctionalInterface
interface GitIssueUrlFormatter {
    fun format(remote: GitRemote, issue: TaskDoc): String
}