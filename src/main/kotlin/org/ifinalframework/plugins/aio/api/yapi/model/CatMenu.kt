package org.ifinalframework.plugins.aio.api.yapi.model

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * 分类菜单
 *
 * @author iimik
 * @since 0.0.1
 **/
data class CatMenu(
    @JsonProperty("_id")
    val id: Long,
    val name: String,
    val list: List<Api>?,
) {
    override fun toString(): String {
        return name
    }
}
