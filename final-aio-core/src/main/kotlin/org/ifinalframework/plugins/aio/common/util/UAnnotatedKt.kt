package org.ifinalframework.plugins.aio.common.util

import org.jetbrains.uast.UAnnotated
import org.jetbrains.uast.UAnnotation


/**
 * UAnnotatedKt
 *
 * @author iimik
 * @since 0.0.5
 **/
object UAnnotatedKt {
    fun UAnnotated.findAnyAnnotation(vararg anns: String): UAnnotation? {
        return anns.firstNotNullOfOrNull { this.findAnnotation(it) }
    }
}