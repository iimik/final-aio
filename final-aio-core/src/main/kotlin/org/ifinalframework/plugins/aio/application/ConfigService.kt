package org.ifinalframework.plugins.aio.application

import com.intellij.openapi.module.Module


/**
 * ConfigService
 *
 * @author iimik
 * @since 0.0.1
 **/
interface ConfigService {
    fun getConfigPaths(module: Module): List<String>
}