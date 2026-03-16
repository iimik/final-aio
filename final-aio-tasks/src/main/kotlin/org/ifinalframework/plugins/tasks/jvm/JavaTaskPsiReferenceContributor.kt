package org.ifinalframework.plugins.tasks.jvm

import com.intellij.openapi.components.service
import com.intellij.openapi.paths.WebReference
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.javadoc.PsiDocTag
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.tasks.TaskDocProcessor
import org.ifinalframework.plugins.aio.tasks.jvm.JvmTaskService
import org.ifinalframework.plugins.tasks.TaskPsiReference
import java.util.concurrent.CancellationException


/**
 * JvmTaskPsiReferenceContributor
 *
 * @author iimik
 * @since 0.0.25
 * @see KTaskPsiReferenceContributor
 **/
class JavaTaskPsiReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiDocTag::class.java),
            object :
                PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<out PsiReference?> {
                    try {
                        val taskDoc = service<JvmTaskService>().getTaskDoc(element) ?: return PsiReference.EMPTY_ARRAY
                        val tagStart = taskDoc.tag.length + 2
                        val tagEnd = tagStart + taskDoc.code.length
                        return arrayOf(TaskPsiReference(element, TextRange(tagStart, tagEnd), taskDoc))
                    } catch (ex: CancellationException) {
                        // ignore
                        return PsiReference.EMPTY_ARRAY
                    }
                }
            })

        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(JavaTokenType.END_OF_LINE_COMMENT),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<out PsiReference?> {
                    try {
                        val taskDoc = service<JvmTaskService>().getTaskDoc(element) ?: return PsiReference.EMPTY_ARRAY
                        val tagStart = taskDoc.tag.length + 2
                        val tagEnd = tagStart + taskDoc.code.length
                        return arrayOf(object : WebReference(element) {
                            override fun getValue(): @NlsSafe String {
                                return service<TaskDocProcessor>().buildUrl(element.project, taskDoc) ?: ""
                            }
                        })
                    } catch (ex: CancellationException) {
                        // ignore
                        return PsiReference.EMPTY_ARRAY
                    }
                }
            })
    }
}