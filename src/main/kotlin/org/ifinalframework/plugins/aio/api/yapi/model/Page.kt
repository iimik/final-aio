package org.ifinalframework.plugins.aio.api.yapi.model


/**
 * Page
 *
 * @author iimik
 * @since 0.0.1
 **/
data class Page<T>(
    val total: Long,
    val count: Long,
    val list: List<T>
)
