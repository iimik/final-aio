package org.ifinalframework.plugins.aio.psi.service.kotlin;

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.jetbrains.kotlin.kdoc.parser.KDocKnownTag
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.kotlin.kdoc.psi.api.KDocElement
import org.jetbrains.kotlin.kdoc.psi.impl.KDocSection
import org.jetbrains.kotlin.kdoc.psi.impl.KDocTag
import org.jetbrains.kotlin.psi.KtDeclaration


/**
 * KotlinDocService
 *
 * @author iimik
 * @since 0.0.1
 **/
class KotlinDocService : DocService {
    override fun getSummary(element: PsiElement): String? {
        return when (element) {
            is KtDeclaration -> {
                val content = element.docComment?.getDefaultSection()?.getContent()?.trimEnd('\n')
                return if (content.isNullOrBlank()) element.name else content
            }

            else -> null
        }
    }

    override fun getTagName(element: PsiElement): String? {
        return if (element is KDocTag) {
            element.name
        } else null
    }

    override fun getTagValue(element: PsiElement): String? {
        return if (element is KDocTag) {
            element.getContent().trim()
        } else null
    }

    override fun findTagValueByTag(element: PsiElement, tag: String): String? {
        if (element == null || tag == null) {
            return null
        }

        val kDoc = findKDoc(element) ?: return null

        val kTag = KDocKnownTag.findByTagName(tag)
        if (kTag != null) {
            val content = R.Read.compute {
                kDoc.findSectionByTag(kTag)?.getContent()
            }
            if (content != null) {
                return content
            }
        }

        return R.Read.compute {
            kDoc.children
                .filterIsInstance<KDocSection>()
                .flatMap { it.findTagsByName(tag) }
                .map { it.getContent() }
                .firstOrNull()
        }

    }

    override fun getLineComment(element: PsiElement): String? {
        return if (element !is KDocElement && element is PsiComment) {
            element.text.replace("//","").trimStart()
        } else null
    }

    private fun findKDoc(element: PsiElement): KDoc? {
        return when (element) {
            is KDoc -> return element
            is KtDeclaration -> element.docComment
            else -> null
        }
    }
}