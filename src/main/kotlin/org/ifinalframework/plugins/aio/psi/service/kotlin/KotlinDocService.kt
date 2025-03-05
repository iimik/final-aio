package org.ifinalframework.plugins.aio.psi.service.kotlin

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocCommentOwner
import com.intellij.psi.PsiElement
import com.intellij.util.containers.stream
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.application.condition.ConditionOnKotlin
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.jetbrains.kotlin.asJava.elements.KtLightElement
import org.jetbrains.kotlin.asJava.elements.KtLightField
import org.jetbrains.kotlin.kdoc.parser.KDocKnownTag
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.kotlin.kdoc.psi.api.KDocElement
import org.jetbrains.kotlin.kdoc.psi.impl.KDocSection
import org.jetbrains.kotlin.kdoc.psi.impl.KDocTag
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtElement
import org.springframework.stereotype.Component
import java.util.*


/**
 * KotlinDocService
 *
 * @author iimik
 * @since 0.0.1
 **/
@Component
@ConditionOnKotlin
class KotlinDocService : DocService {
    override fun getSummary(element: PsiElement): String? {
        return when (element) {
            is KtDeclaration -> {
                val content = element.docComment?.getDefaultSection()?.getContent()?.trimEnd('\n')
                return if (content.isNullOrBlank()) element.name else content
            }

            is KtLightElement<out KtElement, *> -> {
                return element.kotlinOrigin?.let { getSummary(it) }
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
        val kDoc = findKDoc(element) ?: return null

        val kTag = KDocKnownTag.findByTagName(tag)
        if (kTag != null) {
            val content = R.computeInRead {
                kDoc.findSectionByTag(kTag)?.getContent()
            }
            if (content != null) {
                return content
            }
        }

        return R.computeInRead {
            kDoc.children
                .filterIsInstance<KDocSection>()
                .flatMap { it.findTagsByName(tag) }
                .map { it.getContent() }
                .firstOrNull()
        }

    }

    override fun getLineComment(element: PsiElement): String? {
        return if (element !is KDocElement && element is PsiComment) {
            element.text.replace("//", "").trimStart()
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