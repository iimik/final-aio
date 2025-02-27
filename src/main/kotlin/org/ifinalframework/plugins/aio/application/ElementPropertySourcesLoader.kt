package org.ifinalframework.plugins.aio.application

import com.intellij.openapi.module.Module


/**
 * ElementPropertySourcesLoader
 *
 * @author iimik
 * @since 0.0.1
 **/
interface ElementPropertySourcesLoader {
    fun load(classLoader: ClassLoader, module: Module)
}