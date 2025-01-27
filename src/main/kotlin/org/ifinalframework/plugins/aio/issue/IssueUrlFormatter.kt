package org.ifinalframework.plugins.aio.issue


/**
 * IssueUrlFormatter
 *
 * @author iimik
 * @since 0.0.1
 **/
interface IssueUrlFormatter<T> {
    fun format(issue: Issue, param: T): String?
}