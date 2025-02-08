package org.ifinalframework.plugins.aio.jvm


/**
 * AnnotationForResolver
 *
 * @author iimik
 * @since 0.0.2
 **/
object AnnotationForResolver {
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    annotation class StringValue(
        val value: String
    )

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    annotation class IntValue(
        val value: Int
    )

    //---
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    annotation class StringArray(
        val value: Array<String>
    )

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    annotation class IntArray(
        val value: kotlin.IntArray
    )

    // --
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.CLASS)
    annotation class StringVararg(
        vararg val value: String
    )
}
