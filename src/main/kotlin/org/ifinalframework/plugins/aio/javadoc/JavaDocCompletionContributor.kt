package org.ifinalframework.plugins.aio.javadoc

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.JavaDocTokenType

/**
 * JavaDocCompletionContributor
 *
 * @author iimik
 * @see com.intellij.codeInsight.completion.JavaDocCompletionContributor
 */
class JavaDocCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(JavaDocTokenType.DOC_TAG_NAME),
            DocCompletionProviders()
        )
    }
}