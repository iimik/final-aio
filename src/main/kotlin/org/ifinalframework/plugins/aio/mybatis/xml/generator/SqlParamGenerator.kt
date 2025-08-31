package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import org.ifinalframework.plugins.aio.psi.service.DocService

/**
 * SqlParamGenerator
 *
 * - #{value,javaType=,typeHandler}
 *
 * @author iimik
 */
@Service
class SqlParamGenerator {
    fun generate(project: Project, psiElement: PsiElement): String {
        val docService = service<DocService>()

        val name = when (psiElement) {
            is PsiField -> psiElement.name
            else -> throw IllegalArgumentException("Unexpected psi element: $psiElement")
        }

        val typeHandler = docService.findTagValueByTag(psiElement, "typeHandler") ?: return name

        return "$name, typeHandler=$typeHandler"

    }
}