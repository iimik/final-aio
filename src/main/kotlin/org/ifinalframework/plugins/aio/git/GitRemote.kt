package org.ifinalframework.plugins.aio.git


/**
 * GitRemote
 *
 * @author iimik
 * @since 0.0.1
 **/
data class GitRemote(
    val name: String,
    val schema: String?,
    val host: String,
    val path: String
)
