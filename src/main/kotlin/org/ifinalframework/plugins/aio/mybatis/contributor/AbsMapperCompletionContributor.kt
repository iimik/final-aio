package org.ifinalframework.plugins.aio.mybatis.contributor

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiPrimitiveType
import com.intellij.util.PlatformIcons
import org.ifinalframework.plugins.aio.core.annotation.AnnotationAttributes
import org.ifinalframework.plugins.aio.jvm.AnnotationService
import org.ifinalframework.plugins.aio.mybatis.MybatisConstants
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.mybatis.xml.dom.IdDomElement
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Statement

/**
 * AbsMapperCompletionContributor
 *
 * @author iimik
 */
abstract class AbsMapperCompletionContributor : CompletionContributor() {

    protected fun addElementForPsiParameter(project: Project, result: CompletionResultSet, element: IdDomElement?) {
        if (element == null || element !is Statement) {
            return
        }

        val method: PsiMethod = project.service<MapperService>().findMethod(element) ?: return

        if (method == null) {
            thisLogger().info("psiMethod null")
            return
        }

        val parameters = method.parameterList.parameters
        parameters.forEach { result.addElement(buildLookupElement(it, null)) }

    }

    private fun buildLookupElement(parameter: PsiParameter, param: AnnotationAttributes?): LookupElement {
        val type = parameter.type
        val annotationAttributes = service<AnnotationService>().findAnnotationAttributes(parameter, MybatisConstants.PARAM)
        val icon = if (type is PsiPrimitiveType) PlatformIcons.PARAMETER_ICON else PlatformIcons.PROPERTY_ICON
        val name = annotationAttributes?.getString("value") ?: parameter.name
        return PrioritizedLookupElement.withPriority(
            LookupElementBuilder.create(name)
                .withTypeText(type.presentableText)
                .withIcon(icon),
            MybatisConstants.PRIORITY
        )
    }

}