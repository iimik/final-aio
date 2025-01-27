package org.ifinalframework.plugins.aio.issue

import org.springframework.boot.context.properties.ConfigurationProperties


/**
 * IssueProperties
 *
 * @author iimik
 * @since 0.0.1
 **/
@ConfigurationProperties("final.issue")
data class IssueProperties(
    /**
     * @key 标签名称
     * @value URL格式
     */
    val urlFormats: Map<String, String>?
){

}
