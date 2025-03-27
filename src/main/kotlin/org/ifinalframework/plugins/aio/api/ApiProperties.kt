package org.ifinalframework.plugins.aio.api

import org.ifinalframework.plugins.aio.api.yapi.YapiProperties
import org.springframework.boot.context.properties.ConfigurationProperties


/**
 * ApiProperties
 *
 * @author iimik
 * @since 0.0.1
 **/
@ConfigurationProperties("final.api")
data class ApiProperties(
    val contextPath: String? = null,
    val yapi: YapiProperties? = null,
    var markdownBasePath: String = "docs/api"
)
