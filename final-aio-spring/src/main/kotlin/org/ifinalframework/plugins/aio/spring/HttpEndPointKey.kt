package org.ifinalframework.plugins.aio.spring


/**
 * HttpEndPointKey
 *
 * @author iimik
 * @since 0.0.25
 **/
data class HttpEndPointKey(
    val httpMethod: String,
    val path: String
)
