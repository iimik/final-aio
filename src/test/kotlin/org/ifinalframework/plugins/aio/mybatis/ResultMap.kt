package org.ifinalframework.plugins.aio.mybatis


/**
 * ResultMap
 *
 * @author iimik
 * @since 0.0.4
 **/
data class ResultMap(
    /**
     * ID
     */
    val id: Long,
    val name: String,
    /**
     * 年龄
     */
    val age: Int,

    val tags: List<String>,
)
