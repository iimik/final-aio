package org.ifinalframework.plugins.aio.tasks

import git4idea.repo.GitRemote


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