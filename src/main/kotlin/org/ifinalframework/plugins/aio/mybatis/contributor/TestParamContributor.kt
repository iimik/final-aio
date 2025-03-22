package org.ifinalframework.plugins.aio.mybatis.contributor

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.PsiMethod
import com.intellij.util.PlatformIcons
import com.intellij.util.ProcessingContext
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils
import org.ifinalframework.plugins.aio.mybatis.MybatisConstants
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.mybatis.xml.dom.IdDomElement
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Statement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

/**
 * TestParamContributor
 *
 * @author iimik
 */
class TestParamContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            XmlPatterns.psiElement()
                .inside(
                    XmlPatterns.xmlAttributeValue()
                        .inside(XmlPatterns.xmlAttribute().withName("test"))
                ),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val position = parameters.position
                    addElementForPsiParameter(
                        position.project,
                        result,
                        MyBatisUtils.findParentIdDomElement(position)
                    )
                }
            })
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TestParamContributor::class.java)

        fun addElementForPsiParameter(project: Project, result: CompletionResultSet, element: IdDomElement?) {
            if (element == null || element !is Statement) {
                return
            }

            val method: PsiMethod = project.service<MapperService>().findMethod(element) ?: return

            if (method == null) {
                logger.info("psiMethod null")
                return
            }

            val parameters = method.parameterList.parameters

            // For a single parameter MyBatis uses its name, while for a multitude they're
            // named as param1, param2, etc. I'll check if the @Param annotation [value] is present
            // and eventually I'll use its text.
            if (parameters.size == 1) {
                val parameter = parameters[0]
                result.addElement(
                    buildLookupElementWithIcon(
                        parameter.name,
                        parameter.type.presentableText
                    )
                )
            } else {
                for (i in parameters.indices) {
                    val parameter = parameters[i]
                    val value: Optional<String> = Optional.ofNullable(parameter.name)
                    result.addElement(
                        buildLookupElementWithIcon(
                            if (value.isPresent) value.get() else "param" + (i + 1),
                            parameter.type.presentableText
                        )
                    )
                }
            }
        }

        private fun buildLookupElementWithIcon(
            parameterName: String,
            parameterType: String
        ): LookupElement {
            return PrioritizedLookupElement.withPriority(
                LookupElementBuilder.create(parameterName)
                    .withTypeText(parameterType)
                    .withIcon(PlatformIcons.PARAMETER_ICON),
                MybatisConstants.PRIORITY
            )
        }
    }
}
