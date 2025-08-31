package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import org.ifinalframework.plugins.aio.datasource.model.Table
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.util.CaseFormatUtils

/**
 * DefaultColumnGenerator
 *
 * @author iimik
 */
class DefaultColumnGenerator : ColumnGenerator {
    override fun generate(
        project: Project,
        psiElement: PsiElement,
        table: Table?
    ): String {


        val docService = service<DocService>()
        val column = docService.findTagValueByTag(psiElement, "column")

        if (column != null && column.isNotEmpty()) {
            return column;
        }

        val name = when (psiElement) {
            is PsiField -> psiElement.name
            else -> throw IllegalArgumentException("${psiElement.javaClass} is not a field")
        }

        return CaseFormatUtils.lowerCamel2LowerUnderscore(name)
    }
}