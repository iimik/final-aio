package org.ifinalframework.plugins.aio.application.condition


/**
 * ConditionOnMarkdown
 *
 * @author iimik
 * @since 0.0.1
 **/
@ConditionOnLanguage("markdown")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConditionOnMarkdown()
