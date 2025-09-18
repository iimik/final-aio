package org.ifinalframework.plugins.aio.tasks

import com.intellij.openapi.project.Project
import com.intellij.tasks.TaskManager
import com.intellij.tasks.TaskRepository
import com.intellij.tasks.TaskRepositoryType
import org.ifinalframework.plugins.aio.resource.AllIcons

/**
 * TaskUtils
 *
 * @author iimik
 */
object TaskUtils {

    private val map = mutableMapOf<String, TaskType>()

    init {
        val taskRepositoryTypes = TaskRepositoryType.getRepositoryTypes()
        for (repositoryType in taskRepositoryTypes) {
            map[repositoryType.name.uppercase()] = TaskType(repositoryType.name, repositoryType.icon)
        }
        map["issue".uppercase()] = TaskType("issue", AllIcons.Issues.ISSUE)
    }

    fun getTaskType(tagName: String): TaskType? {
        return map[tagName.uppercase()]
    }

    fun getTaskRepository(project: Project, type: String): TaskRepository? {
        for (taskRepository in TaskManager.getManager(project).allRepositories) {
            if (type.equals(taskRepository.repositoryType.name, ignoreCase = true)) {
                return taskRepository
            }
        }

        return null;
    }


}