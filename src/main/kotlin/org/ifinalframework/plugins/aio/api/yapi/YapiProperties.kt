package org.ifinalframework.plugins.aio.api.yapi

import org.springframework.boot.context.properties.ConfigurationProperties


/**
 * YapiProperties
 *
 * @author iimik
 * @since 0.0.2
 **/
@ConfigurationProperties("final.api.yapi")
data class YapiProperties(
    val serverUrl: String?,
    val token: String?,
)
