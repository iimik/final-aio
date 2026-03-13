package org.ifinalframework.plugins.aio.javadoc

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.util.ProcessingContext


/**
 * JVM 语言文档注释补全提供者
 *
 * @author iimik
 * @since 0.0.24
 * @see JavaDocCompletionContributor
 * @see KDocCompletionContributor
 **/
interface JvmDocCompletionProvider {
    fun addDocTagCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    )

    fun addEndOfLineCommentCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    )
}