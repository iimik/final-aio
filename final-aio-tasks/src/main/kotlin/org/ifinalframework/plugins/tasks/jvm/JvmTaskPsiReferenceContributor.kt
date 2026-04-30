package org.ifinalframework.plugins.tasks.jvm

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.components.service
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.javadoc.PsiDocTag
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.tasks.jvm.JvmTaskService
import org.ifinalframework.plugins.tasks.TaskPsiReference
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.lexer.KtTokens
import java.util.concurrent.CancellationException


/**
 * JVM 语言 Task 引用贡献者
 *
 * |   语言   | 文档注释 | 行尾注释 | 说明 |
 * |:------:|:----:|:----:|:--:|
 * |  Java  |    ✅   |    ✅   |    |
 * | Kotlin |    ❌   |    ✅   |    |
 *
 *
 * @author iimik
 * @since 0.0.25
 **/
class JvmTaskPsiReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        // ✅java doc tag
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiDocTag::class.java).withLanguage(JavaLanguage.INSTANCE),
            docTagTaskPsiReferenceProvider
        )

        // ✅java eof comment
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(JavaTokenType.END_OF_LINE_COMMENT).withLanguage(JavaLanguage.INSTANCE),
            endOfLineCommentPsiReferenceProvider
        )

        // ✅kotlin eof comment
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(KtTokens.EOL_COMMENT).withLanguage(KotlinLanguage.INSTANCE),
            endOfLineCommentPsiReferenceProvider
        )
    }

    private val docTagTaskPsiReferenceProvider = object : PsiReferenceProvider() {
        override fun getReferencesByElement(
            element: PsiElement, context: ProcessingContext
        ): Array<out PsiReference?> {
            try {
                val taskDoc = service<JvmTaskService>().getTaskDoc(element) ?: return PsiReference.EMPTY_ARRAY
                val tagStart = taskDoc.tag.length + 2
                val tagEnd = tagStart + taskDoc.code.length
                return arrayOf(TaskPsiReference(element, TextRange(tagStart, tagEnd), taskDoc))
            } catch (_: CancellationException) {
                // ignore
                return PsiReference.EMPTY_ARRAY
            }
        }
    }

    private val endOfLineCommentPsiReferenceProvider = object : PsiReferenceProvider() {
        override fun getReferencesByElement(
            element: PsiElement, context: ProcessingContext
        ): Array<out PsiReference?> {
            try {
                val taskDoc = service<JvmTaskService>().getTaskDoc(element) ?: return PsiReference.EMPTY_ARRAY
                val text = element.text
                val start = text.indexOfAny(charArrayOf('@', '#'))
                val tagStart = start + taskDoc.tag.length + 2
                val tagEnd = tagStart + taskDoc.code.length

                return arrayOf(TaskPsiReference(element, TextRange(tagStart, tagEnd), taskDoc))
            } catch (_: CancellationException) {
                // ignore
                return PsiReference.EMPTY_ARRAY
            }
        }
    }

}