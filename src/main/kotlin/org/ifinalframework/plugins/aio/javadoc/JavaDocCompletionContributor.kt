package org.ifinalframework.plugins.aio.javadoc

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.JavaDocTokenType
import com.intellij.psi.JavaTokenType


/**
 * JavaDocCompletionContributor
 *
 * @author iimik
 * @see com.intellij.codeInsight.completion.JavaDocCompletionContributor
 */
class JavaDocCompletionContributor : CompletionContributor() {
    init {
        /**
         * 文档注释
         */
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(JavaDocTokenType.DOC_TAG_NAME),
            DocCompletionProviders()
        )

        /**
         * 单行注释
         */
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(JavaTokenType.END_OF_LINE_COMMENT),
            DocCompletionProviders()
        )
    }

}