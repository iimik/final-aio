package org.ifinalframework.plugins.aio.git;

import com.intellij.openapi.project.Project
import git4idea.repo.GitRemote
import git4idea.repo.GitRepositoryManager
import org.springframework.util.CollectionUtils
import java.util.stream.Collectors


/**
 * DefaultGitService
 *
 * @author iimik
 * @since 0.0.1
 **/
class DefaultGitService(
    val project: Project
) : GitService {

    override fun getRemotes(): List<GitRemote> {
        val gitRepositoryManager = GitRepositoryManager.getInstance(project)
        val repositories = gitRepositoryManager.repositories
        if (CollectionUtils.isEmpty(repositories)) {
            return emptyList()
        }

        return repositories.stream()
            .flatMap { it.remotes.stream() }
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

}