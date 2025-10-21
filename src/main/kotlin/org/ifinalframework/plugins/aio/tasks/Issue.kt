package org.ifinalframework.plugins.aio.tasks


/**
 * Issue
 *
 * ## 文档注释
 *
 * * `@issue code desc`
 * * `@jira code desc`
 *
 * ## 行注释
 *
 * * `@jira code desc`
 * * `@issue code desc`
 * * `#code desc`
 *
 * @author iimik
 * @since 0.0.1
 **/
data class Issue(
    /**
     * 类型
     */
    val type: IssueType,
    /**
     * 编码
     */
    val code: String,
    /**
     * 描述
     */
    val description: String? = null,
)