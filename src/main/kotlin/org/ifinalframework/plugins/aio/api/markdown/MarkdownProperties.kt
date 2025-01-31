package org.ifinalframework.plugins.aio.api.markdown

import org.springframework.boot.context.properties.ConfigurationProperties


/**
 * MarkdownProperties
 *
 * @author iimik
 * @since 0.0.2
 **/
@ConfigurationProperties("final.api.markdown")
data class MarkdownProperties(
    /**
     * Markdown根目录
     */
    val basePath: String = "docs/api",
)
