package org.ifinalframework.plugins.aio.core

import kotlin.reflect.KClass


/**
 * ConfigItem
 *
 * @author iimik
 * @since 0.0.6
 **/
data class ConfigItem<T : Any>(
    val key: String,
    val type: KClass<T>,
    val defaultValue: T? = null,
)