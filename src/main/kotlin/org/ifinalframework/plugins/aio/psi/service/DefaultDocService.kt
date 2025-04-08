package org.ifinalframework.plugins.aio.psi.service

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.util.SpiUtil

/**
 * DefaultDocService
 *
 * @author iimik
 */
class DefaultDocService() : DocService {

    private val docService = SpiUtil.languageSpi<DocService>()

    override fun getSummary(element: PsiElement): String? {
        return docService.getSummary(element)
    }

    override fun getTagName(element: PsiElement): String? {
        return docService.getTagName(element)
    }

    override fun getTagValue(element: PsiElement): String? {
        return docService.getTagValue(element)
    }

    override fun findTagValueByTag(element: PsiElement, tag: String): String? {
        return docService.findTagValueByTag(element, tag)
    }

    override fun getLineComment(element: PsiElement): String? {
        return docService.getLineComment(element)
    }
}