package org.ifinalframework.plugins.aio.application.condition

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty


/**
 * ConditionOnLanguage
 *
 * @author iimik
 * @since 0.0.1
 **/
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ConditionalOnProperty(prefix = "final", name = ["jvm"], havingValue = "true", matchIfMissing = false)
annotation class ConditionOnJvm(
)
