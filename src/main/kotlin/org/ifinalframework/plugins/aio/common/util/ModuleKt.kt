package org.ifinalframework.plugins.aio.common.util

import com.intellij.openapi.project.guessModuleDir
import com.intellij.openapi.module.Module


/**
 * ModuleKt
 *
 * @author iimik
 * @since 0.0.1
 **/

fun Module.getBasePath(): String {
    return guessModuleDir()!!.path;
}