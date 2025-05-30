package org.ifinalframework.plugins.aio.mybatis

import java.util.*


/**
 * ResultMap
 *
 * @author iimik
 * @since 0.0.4
 **/
data class ResultMap(
    /**
     * ID
     *
     * @since 0.0.1
     * @author iimik
     */
    val id: Long,
    val name: String,
    /**
     * 年龄
     */
    val age: Int,

    val tags: List<String>,

    val startDate: Date,
    val endDate: Date,
){
    var haha: Int? = null

    fun setHaha2(haha: String){

    }
}
