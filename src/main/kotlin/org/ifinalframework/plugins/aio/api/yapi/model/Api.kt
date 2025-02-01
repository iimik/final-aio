package org.ifinalframework.plugins.aio.api.yapi.model

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Api
 *
 * ```json
 * {
 *     "edit_uid": 0,
 *     "status": "done",
 *     "api_opened": false,
 *     "tag": [],
 *     "_id": 50734,
 *     "method": "GET",
 *     "catid": 9273,
 *     "title": "AAA",
 *     "path": "/api/",
 *     "project_id": 96,
 *     "uid": 1142,
 *     "add_time": 1674110433
 * }
 * ```
 *
 * @author iimik
 * @since 0.0.2
 **/
data class Api(
    @JsonProperty("_id")
    val id: Long,
    val title: String,
    val method: String,
    val path: String,
    /**
     * 项目ID
     */
    @JsonProperty("project_id")
    /**
     * 分类ID
     */
    val projectId: Long,
    @JsonProperty("catid")
    val catId: Long
)
