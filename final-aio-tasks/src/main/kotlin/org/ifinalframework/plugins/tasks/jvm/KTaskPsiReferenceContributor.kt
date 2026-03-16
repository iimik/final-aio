package org.ifinalframework.plugins.tasks.jvm

import com.intellij.openapi.components.service
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.tasks.jvm.JvmTaskService
import org.ifinalframework.plugins.tasks.TaskPsiReference
import org.jetbrains.kotlin.lexer.KtTokens
import java.util.concurrent.CancellationException


/**
 * KotlinTaskPsiReferenceContributor
 *
 * @author iimik
 * @since 0.0.25
 * @see JavaTaskPsiReferenceContributor
 **/
class KTaskPsiReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(KtTokens.EOL_COMMENT),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<out PsiReference?> {
                    try {
                        val taskDoc = service<JvmTaskService>().getTaskDoc(element) ?: return PsiReference.EMPTY_ARRAY
                        val text = element.text
                        val start = text.indexOfAny(charArrayOf('@', '#'))
                        val tagStart = start + taskDoc.tag.length + 2
                        val tagEnd = tagStart + taskDoc.code.length

                        return arrayOf(TaskPsiReference(element, TextRange(tagStart, tagEnd), taskDoc))
                    } catch (ex: CancellationException) {
                        // ignore
                        return PsiReference.EMPTY_ARRAY
                    }
                }
            })
    }
}