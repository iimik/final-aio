package org.ifinalframework.plugins.aio.application.condition

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.annotation.AliasFor


/**
 * ConditionOnLanguage
 *
 * @author iimik
 * @since 0.0.1
 **/
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ConditionalOnProperty(prefix = "final", name = ["language"], matchIfMissing = false)
annotation class ConditionOnLanguage(
    @get:AliasFor(annotation = ConditionalOnProperty::class, attribute = "havingValue")
    val value: String = ""
)
