package org.ifinalframework.plugins.aio.psi.service

import com.intellij.psi.PsiElement


/**
 * DocService
 *
 * @author iimik
 * @since 0.0.1
 **/
interface DocService {

    fun getSummary(element: PsiElement): String?

    fun getTagName(element: PsiElement): String?

    fun getTagValue(element: PsiElement): String?

    fun findTagValueByTag(element: PsiElement, tag: String): String?

    fun getLineComment(element: PsiElement): String?
}