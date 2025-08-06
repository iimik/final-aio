package org.ifinalframework.plugins.aio.mybatis.contributor

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.util.PlatformIcons
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.service.PsiService

/**
 * SQL 参数补全
 *
 * @issue 32
 * @author iimik
 * @since 0.0.7
 */
class MapperSqlParamCompletionContributor : AbsMapperCompletionContributor() {
    val docService = service<DocService>()
    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (parameters.completionType != CompletionType.BASIC) {
            return
        }

        val position = parameters.position
        val topLevelFile = position.containingFile.context?.containingFile ?: return
        if (MyBatisUtils.isMapperFile(topLevelFile)) {
            val shouldAddElement = shouldAddElement(position.containingFile, parameters.offset)
            if (shouldAddElement) {
                process(topLevelFile, result, position)
            }
        }
    }

    private fun process(xmlFile: PsiFile, result: CompletionResultSet, position: PsiElement) {
        val psiFile = position.containingFile
        val context = position.containingFile.context ?: return
        val statement = MyBatisUtils.findStatementDomElement(context) ?: return

        val method = statement.getId().value ?: return
        val params = method.parameterList.parameters
        if (params.size == 1) {
            val type = params[0].type
            processParam(null, type, result, position.project)
        } else {
            params.forEach { param ->
                val prefix = param.name
                val type = param.type
                processParam(prefix, type, result, position.project)
            }
        }

        result.stopHere()

    }


    private fun processParam(prefix: String?, type: PsiType, result: CompletionResultSet, project: Project) {

        val testCompletion = project.service<MyBatisProperties>().testCompletion

        if (type is PsiClassReferenceType) {
            val className = type.reference.qualifiedName
            if (className.startsWith("java.lang.") || className.startsWith("java.util.")) {
                val name = prefix ?: "value"
                result.addElement(
                    LookupElementBuilder.create(name)
                        .withIcon(PlatformIcons.PARAMETER_ICON)
                        .withCaseSensitivity(false)
                )
            } else {
                val clazz = project.service<PsiService>().findClass(className) ?: return
                clazz.allFields
                    .filter { !it.hasModifierProperty(PsiModifier.STATIC) }
                    .forEach { field ->
                        var typeText = field.type.presentableText

                        docService.getSummary(field)?.let { typeText = "$it ($typeText)" }

                        result.addElement(
                            LookupElementBuilder.create(field.name)
                                .withIcon(PlatformIcons.PROPERTY_ICON)
                                .withTypeText(typeText)
                                .withCaseSensitivity(false)
                        )

                    }
            }
        } else if (type is PsiPrimitiveType) {
            val name = prefix ?: "value"
            result.addElement(
                LookupElementBuilder.create(name)
                    .withIcon(PlatformIcons.PARAMETER_ICON)
                    .withCaseSensitivity(false)
            )
        }

    }

    private fun shouldAddElement(file: PsiFile, offset: Int): Boolean {
        val text = file.text
        for (i in offset - 1 downTo 1) {
            val c = text[i]
            if (c == '{' && (text[i - 1] == '#' || text[i - 1] == '$')) return true
        }
        return false
    }
}