package org.ifinalframework.plugins.aio.application.condition


/**
 * ConditionOnJava
 *
 * @author iimik
 * @since 0.0.1
 **/
@ConditionOnLanguage("java")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConditionOnJava()
