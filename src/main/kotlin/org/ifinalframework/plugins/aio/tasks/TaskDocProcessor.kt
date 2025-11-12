package org.ifinalframework.plugins.aio.tasks

import com.intellij.openapi.project.Project
import com.intellij.tasks.Task

/**
 * TaskDocProcessor
 *
 * @author iimik
 */
interface TaskDocProcessor {

    fun buildTaskDoc(task: Task): org.ifinalframework.plugins.aio.tasks.TaskDoc?

    fun buildUrl(project: Project, taskDoc: org.ifinalframework.plugins.aio.tasks.TaskDoc): String?

}