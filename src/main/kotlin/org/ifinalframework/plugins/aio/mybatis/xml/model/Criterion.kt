package org.ifinalframework.plugins.aio.mybatis.xml.model

/**
 * Criterion
 *
 * @author iimik
 */
data class Criterion(
    val column: String,
    val sqlParam: String,
    val test: String,
)
