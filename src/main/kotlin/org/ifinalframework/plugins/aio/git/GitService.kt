package org.ifinalframework.plugins.aio.git


/**
 * GitService
 *
 * @author iimik
 * @since 0.0.1
 **/
interface GitService {
    fun getRemotes(): List<GitRemote>

    fun getDefaultRemote(): GitRemote

    fun getRemoteByNameOrDefault(name: String): GitRemote
}