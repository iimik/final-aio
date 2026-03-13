package org.ifinalframework.plugins.aio.psi.service.java;

import com.intellij.psi.*
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.javadoc.PsiDocTag
import com.intellij.util.containers.stream
import org.ifinalframework.plugins.aio.application.condition.ConditionOnJava
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.springframework.stereotype.Component
import java.util.*


/**
 * JavaDocService
 *
 * @author iimik
 * @since 0.0.1
 **/
@Component
@ConditionOnJava
class JavaDocService : DocService {
    private val DOC_COMMENT_PREFIXES = listOf<String>(
        "*", "///", "//"
    )

    override fun getSummary(element: PsiElement): String? {
        val docComment = if (element is PsiDocCommentOwner) element.docComment else return null
        if (Objects.isNull(docComment)) return null
        return docComment?.descriptionElements.stream()
            .map { it.text.trim() }
            .filter { it != null && it.isNotEmpty() }
            .findFirst().orElse(null)

    }

    private fun getDocComment(element: PsiElement): PsiDocComment? {
        return when (element) {
            is PsiDocComment -> {
                element
            }

            is PsiJavaDocumentedElement -> {
                element.docComment
            }

            else -> return null
        }
    }

    override fun getTagName(element: PsiElement): String? {
        return if (element is PsiDocTag) {
            element.name
        } else null
    }

    override fun getTagValue(element: PsiElement): String? {
        return if (element is PsiDocTag) {
            element.docValue()
        } else null
    }

    private fun PsiDocTag.docValue(discardName: Boolean = false): String {
        val lines = this.text.lines()
        if (lines.isEmpty()) return ""
        var ret = lines[0].removePrefix(this.nameElement.text).trimStart()
        if (discardName) {
            this.valueElement?.text?.let {
                ret = ret.removePrefix(it).trimStart()
            }
        }
        if (lines.size == 1) {
            return ret
        }
        for (i in 1 until lines.size) {
            lines[i].trim()
                .removeCommentPrefix()
                .takeIf { it.isNotBlank() }
                ?.let {
                    ret = "$ret\n$it"
                }
        }
        return ret
    }

    /**
     * Removes any documentation comment prefix from the beginning of a line.
     * This is used to clean up documentation text by removing comment markers
     * like "*", "///", or "//" from the start of each line.
     *
     * @return The line text with any matching prefix removed and trimmed
     */
    private fun String.removeCommentPrefix(): String {
        for (prefix in DOC_COMMENT_PREFIXES) {
            if (this.startsWith(prefix)) {
                return this.removePrefix(prefix).trim()
            }
        }
        return this
    }

    override fun hasTag(element: PsiElement, tag: String): Boolean {
        val psiDocComment = getDocComment(element) ?: return false
        val docTags = psiDocComment.tags
        return docTags.stream().filter { it is PsiDocTag && it.name.equals(tag, ignoreCase = true) }.findAny().isPresent
    }

    override fun findTagValueByTag(element: PsiElement, tag: String): String? {
        val psiDocComment = getDocComment(element) ?: return null
        val docTags = psiDocComment.tags
        return docTags.stream().filter { it is PsiDocTag && it.name.equals(tag, ignoreCase = true) }
            .map { it.valueElement?.text?.trim() }
            .findFirst().orElse(null)
    }

    override fun getLineComment(element: PsiElement): String? {
        return if (element !is PsiDocComment && element is PsiComment) {
            element.text
        } else null
    }
}