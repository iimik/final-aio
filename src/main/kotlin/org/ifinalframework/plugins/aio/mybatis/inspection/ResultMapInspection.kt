package org.ifinalframework.plugins.aio.mybatis.inspection

import com.google.common.base.CaseFormat
import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import org.ifinalframework.plugins.aio.intellij.GenericQuickFix
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.mybatis.xml.dom.ResultMap
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.jetbrains.uast.UField
import org.jetbrains.uast.getContainingUClass

/**
 * ResultMap 检查
 *
 * 检查ResultMap类中的属性(`Field`)是否有在`<resultMap>`中定义，并提供快速修复方式。
 *
 * @author iimik
 * @see org.ifinalframework.plugins.aio.mybatis.xml.dom.ResultMap
 */
class ResultMapInspection : AbstractBaseUastLocalInspectionTool() {
    override fun checkField(field: UField, manager: InspectionManager, isOnTheFly: Boolean): Array<out ProblemDescriptor?>? {

        val uClass = field.getContainingUClass() ?: return null
        val qualifiedName = uClass.qualifiedName ?: return null
        val resultMaps = manager.project.service<MapperService>().findResultMaps(qualifiedName)
        if(resultMaps.isEmpty()) return null

        if (resultMaps
                .flatMap { it.getProperties() }
                .firstOrNull { it.getProperty().stringValue == field.name } == null
        ) {


            val problemDescriptor = manager.createProblemDescriptor(
                field.sourcePsi!!,
                "ResultMap with type=\"${qualifiedName}\" not defined property of \"${field.name}\".",
                ResultMapPropertyNotExistsQuickFix(field),
                ProblemHighlightType.ERROR,
                isOnTheFly
            )
            return arrayOf(problemDescriptor)
        }



        return super.checkField(field, manager, isOnTheFly)
    }

    private class ResultMapPropertyNotExistsQuickFix(val field: UField) : GenericQuickFix() {
        override fun getFamilyName(): String {
            return I18N.message("MyBatis.ResultMapPropertyNotExistsQuickFix.name")
        }
        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val clazz = field.getContainingUClass() ?: return
            val className = clazz.qualifiedName ?: return
            val resultMaps = project.service<MapperService>().findResultMaps(className).toList()
            if (resultMaps.size == 1) {
                val property = resultMaps.first().addResult()
                property.getProperty().stringValue = field.name
            } else if (resultMaps.size > 1) {
                JBPopupFactory.getInstance().createListPopup(
                    object : BaseListPopupStep<ResultMap>(
                        "[ Select ResultMap to add property: " + field.name + "]",
                        resultMaps,
                        AllIcons.Mybatis.JVM
                    ) {

                        override fun getTextFor(value: ResultMap): String {
                            return """
                                <resultMap id="${value.getId().stringValue}" type="${value.getType().stringValue}">
                            """.trimIndent()
                        }
                        override fun onChosen(resultMap: ResultMap, finalChoice: Boolean): PopupStep<*>? {
                            WriteCommandAction.runWriteCommandAction(project) {
                                val property = resultMap.addResult()
                                val column = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.name)
                                property.getColumn().stringValue = column
                                property.getProperty().stringValue = field.name
                            }
                            return PopupStep.FINAL_CHOICE
                        }
                    }
                ).showInFocusCenter()
            }

        }
    }


}