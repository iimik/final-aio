package org.ifinalframework.plugins.aio.api

import org.ifinalframework.plugins.aio.core.ConfigItem


/**
 * ApiConfigs
 *
 * @author iimik
 * @since 0.0.6
 **/
object ApiConfigs {
    val MarkdownBasePath = ConfigItem<String>("final.api.markdown.basePath", String::class, "docs/api")
}