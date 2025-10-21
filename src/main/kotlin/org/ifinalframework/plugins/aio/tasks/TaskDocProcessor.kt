package org.ifinalframework.plugins.aio.tasks

import com.intellij.openapi.project.Project
import com.intellij.tasks.Task

/**
 * TaskDocProcessor
 *
 * @author iimik
 */
interface TaskDocProcessor {

    fun buildTaskDoc(task: Task): TaskDoc?

    fun buildUrl(project: Project,taskDoc: TaskDoc): String?

}