package org.ifinalframework.plugins.aio.common.util

import com.intellij.openapi.project.Project


/**
 * ProjectKt
 *
 * @author iimik
 * @since 0.0.5
 **/
inline fun <reified T : Any> Project.getService(): T {
    return this.getService(T::class.java)
}
