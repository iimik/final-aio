package org.ifinalframework.plugins.aio.psi.service

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.application.annotation.ReadAction
import org.ifinalframework.plugins.aio.psi.service.java.JavaDocService
import org.ifinalframework.plugins.aio.psi.service.kotlin.KotlinDocService
import org.ifinalframework.plugins.aio.spi.annotation.LanguageSpi


/**
 * DocService
 *
 * @author iimik
 * @since 0.0.1
 **/
@LanguageSpi<DocService>(
    JavaDocService::class,
    KotlinDocService::class
)
@ReadAction
interface DocService {

    fun getSummary(element: PsiElement): String?

    fun getTagName(element: PsiElement): String?

    fun getTagValue(element: PsiElement): String?

    fun findTagValueByTag(element: PsiElement, tag: String): String?

    fun getLineComment(element: PsiElement): String?
}