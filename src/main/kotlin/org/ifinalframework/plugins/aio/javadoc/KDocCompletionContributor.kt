package org.ifinalframework.plugins.aio.javadoc

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens
import org.jetbrains.kotlin.lexer.KtTokens

/**
 * JavaDocCompletionContributor
 *
 * @author iimik
 * @see org.jetbrains.kotlin.idea.completion.KDocCompletionContributor
 */
class KDocCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().afterLeaf(
                StandardPatterns.or(
                    PlatformPatterns.psiElement(KDocTokens.LEADING_ASTERISK),
                    PlatformPatterns.psiElement(KDocTokens.START)
                )
            ),
            DocCompletionProviders()
        )

        /**
         * 文档注释
         */
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(KDocTokens.TAG_NAME),
            DocCompletionProviders()
        )

        /**
         * 单行注释
         */
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(KtTokens.EOL_COMMENT),
            DocCompletionProviders()
        )

    }
}