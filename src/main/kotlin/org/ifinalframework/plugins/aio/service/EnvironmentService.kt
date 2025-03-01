package org.ifinalframework.plugins.aio.service

import com.intellij.openapi.module.Module
import org.ifinalframework.plugins.aio.core.ConfigItem
import kotlin.reflect.KClass


/**
 * EnvironmentService
 *
 * @author iimik
 * @since 0.0.5
 **/
interface EnvironmentService {

    fun getProperty(module: Module, key: String): String?

    fun <T : Any> getProperty(module: Module, key: String, type: KClass<T>): T?

    fun <T : Any> getProperty(module: Module, key: String, clazz: KClass<T>, value: T): T

    fun <T : Any> getProperty(module: Module, configItem: ConfigItem<T>): T? {
        return getProperty(module, configItem.key, configItem.type) ?: configItem.defaultValue
    }

    fun reset()
}