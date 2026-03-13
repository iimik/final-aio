package org.ifinalframework.plugins.aio.javadoc

import com.intellij.openapi.extensions.ExtensionPointName


/**
 * JvmDocCompletionProviders
 *
 * @author iimik
 * @since 1.6.0
 **/
object JvmDocCompletionProviders {

    private val EP = ExtensionPointName<JvmDocCompletionProvider>("org.ifinalframework.plugins.aio.javadoc.jvmDocCompletionProvider")

    fun providers(): List<JvmDocCompletionProvider> {
        return EP.extensionList
    }
}