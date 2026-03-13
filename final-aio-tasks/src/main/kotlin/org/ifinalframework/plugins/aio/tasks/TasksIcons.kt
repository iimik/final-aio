package org.ifinalframework.plugins.aio.tasks

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon


/**
 * AllIcons
 *
 * @author iimik
 * @since 0.0.24
 * @see [icons](https://plugins.jetbrains.com/docs/intellij/icons.html)
 **/
class TasksIcons {

    companion object {
        val TASK = load("assets/icons/tasks.svg")
        val ISSUE = load("assets/icons/git.svg")
        val JIRA = load("assets/icons/jira.svg")


        private fun load(path: String): Icon {
            return IconLoader.getIcon(path, TasksIcons::class.java)
        }
    }
}