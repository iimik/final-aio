package org.ifinalframework.plugins.aio.api.yapi.model

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Project
 *
 * @author iimik
 * @since 0.0.1
 **/
data class Project(
    @JsonProperty("_id")
    val id: Long,
    val name: String?,
)
