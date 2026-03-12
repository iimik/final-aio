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
     * @typeHandler haha
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
    /**
     * haha
     * @typeHandler haha
     */
    val startDate: Date,
    val endDate: Date,
){
    var haha: Int? = null

    var nullOrNotNull: NullOrNotNull? = null

    fun setHaha2(haha: String){

    }
}
