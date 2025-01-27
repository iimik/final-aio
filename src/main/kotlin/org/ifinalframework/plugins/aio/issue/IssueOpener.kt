package org.ifinalframework.plugins.aio.issue


/**
 * IssueOpener
 *
 * @author iimik
 * @since 0.0.1
 **/
@FunctionalInterface
interface IssueOpener {
    fun open(issue: Issue)
}