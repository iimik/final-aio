package org.ifinalframework.plugins.aio.mybatis.xml.generator.criterion

import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.intellij.psi.PsiParameter
import org.ifinalframework.plugins.aio.jvm.AnnotationService
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.util.CaseFormatUtils

/**
 * AbstractCriterionGenerator
 *
 * @author iimik
 */
abstract class AbstractCriterionGenerator {
    private val docService = service<DocService>()
    private val annotationService = service<AnnotationService>()

    protected fun getName(psiElement: PsiElement): String {
        return when (psiElement) {
            is PsiField -> psiElement.name
            is PsiParameter -> psiElement.name
            else -> throw IllegalArgumentException("${psiElement.javaClass} is not a field")
        }
    }

    protected fun generateColumn(psiElement: PsiElement): String {
        val column = docService.findTagValueByTag(psiElement, "column")

        if (!column.isNullOrEmpty()) {
            return column;
        }

        val name = getName(psiElement)

        return generateColumn(name)

    }

    protected fun generateColumn(name: String): String{
        return when (name) {
            "id" -> "id"
            "ids" -> "id"
            else -> CaseFormatUtils.lowerCamel2LowerUnderscore(name)
        }
    }


}