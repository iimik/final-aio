package org.ifinalframework.plugins.aio.api.yapi.model

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * 分类菜单
 *
 * ```json
 *         {
 *             "index": 0,
 *             "_id": 1,
 *             "name": "分类名称",
 *             "project_id": 1,
 *             "desc": null,
 *             "uid": 1,
 *             "add_time": 1674103051,
 *             "up_time": 1674103051,
 *             "__v": 0
 *         }
 * ```
 *
 * @author iimik
 * @since 0.0.1
 **/
data class CatMenu(
    @JsonProperty("_id")
    val id: Long,
    val name: String,
    @JsonProperty("project_id")
    val projectId: Long,
    val list: List<Api>?,
) {
    override fun toString(): String {
        return name
    }
}
