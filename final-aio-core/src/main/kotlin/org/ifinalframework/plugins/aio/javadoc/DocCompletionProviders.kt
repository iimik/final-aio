package org.ifinalframework.plugins.aio.javadoc

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext

/**
 * JavaDocCompletionContributor
 * @author iimik
 * @since 0.0.24
 */
object DocCompletionProviders {

    val DOG_TAG = DocTagCompletionProvider();
    val END_OF_LINE_COMMENT = EndOfLineCommentCompletionProvider()

    class DocTagCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            p0: CompletionParameters,
            p1: ProcessingContext,
            p2: CompletionResultSet
        ) {
            for (provider in JvmDocCompletionProviders.providers()) {
                provider.addDocTagCompletions(p0, p1, p2)
            }
        }
    }

    class EndOfLineCommentCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            p0: CompletionParameters,
            p1: ProcessingContext,
            p2: CompletionResultSet
        ) {
            for (provider in JvmDocCompletionProviders.providers()) {
                provider.addDocTagCompletions(p0, p1, p2)
            }
        }
    }
}