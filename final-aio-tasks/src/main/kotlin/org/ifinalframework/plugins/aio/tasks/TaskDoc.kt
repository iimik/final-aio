package org.ifinalframework.plugins.aio.tasks

import javax.swing.Icon

/**
 * Task
 *
 * @author iimik
 */
data class TaskDoc(
    val tag: String,
    val code: String,
    val summary: String,
    val icon: Icon
)
