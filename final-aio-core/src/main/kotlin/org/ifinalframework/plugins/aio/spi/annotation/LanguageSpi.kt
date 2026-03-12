package org.ifinalframework.plugins.aio.spi.annotation

import kotlin.reflect.KClass


/**
 * LanguageSpi
 *
 * @author iimik
 * @since 0.0.1
 **/
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LanguageSpi<T : Any>(
    vararg val value: KClass<out T> = [],
)
