package org.ifinalframework.plugins.aio.issue

import org.springframework.boot.context.properties.ConfigurationProperties


/**
 * JiraIssueProperties
 *
 * @author iimik
 * @since 0.0.1
 **/
@ConfigurationProperties("final.issue.jira")
data class JiraIssueProperties(
    /**
     * 服务地址
     */
    val serverUrl: String?,
    /**
     * 项目编码
     */
    val projectCode: String?
)
