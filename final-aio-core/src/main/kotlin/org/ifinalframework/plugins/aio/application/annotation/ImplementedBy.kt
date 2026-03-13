package org.ifinalframework.plugins.aio.application.annotation

import kotlin.reflect.KClass


/**
 * ImplementedBy
 *
 * @author iimik
 * @since 0.0.2
 **/
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ImplementedBy(
    vararg val value: KClass<*>
)
