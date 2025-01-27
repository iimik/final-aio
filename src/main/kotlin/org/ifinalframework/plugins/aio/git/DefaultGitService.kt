package org.ifinalframework.plugins.aio.git;

import com.intellij.openapi.project.Project
import org.eclipse.jgit.api.Git
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils
import java.io.File
import java.util.stream.Collectors


/**
 * DefaultGitService
 *
 * @author iimik
 * @since 0.0.1
 **/
@Component
class DefaultGitService(
    private val gitDir: String
) : GitService {

    @Autowired
    constructor(project: Project) : this(project.basePath!!)

    override fun getRemotes(): List<GitRemote> {
        val git = getGit()
        val remoteConfigs = git.remoteList().call()
        if (CollectionUtils.isEmpty(remoteConfigs)) {
            return emptyList()
        }

        return remoteConfigs.stream()
            .flatMap { it.urIs.stream().map { uri -> GitRemote(it.name, uri.scheme, uri.host, uri.path.removeSuffix(".git")) } }
            .toList()

    }

    override fun getDefaultRemote(): GitRemote {
        val remotes = getRemotes()
        if (remotes.size == 1) {
            return remotes[0]
        }

        val remote = remotes.firstOrNull { it.name == "origin" }

        if (remote != null) {
            return remote
        }

        return remotes[0]

    }

    override fun getRemoteByNameOrDefault(name: String): GitRemote {
        val remotes = getRemotes()
        val map = remotes.stream().collect(Collectors.toMap({ it.name }) { it })
        return map[name] ?: getDefaultRemote()

    }

    private fun getGit(): Git {
        return Git.open(File(gitDir))
    }

}