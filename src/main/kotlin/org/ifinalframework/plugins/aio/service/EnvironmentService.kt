package org.ifinalframework.plugins.aio.service

import com.intellij.openapi.module.Module
import kotlin.reflect.KClass


/**
 * EnvironmentService
 *
 * @author iimik
 * @since 0.0.5
 **/
interface EnvironmentService {
    fun getProperty(module: Module, key: String): String?

    fun <T : Any> getProperty(module: Module, key: String, clazz: KClass<T>, value: T): T

    fun reset()
}