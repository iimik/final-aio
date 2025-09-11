package org.ifinalframework.plugins.aio.api.inspection

import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.components.service
import org.ifinalframework.plugins.aio.api.service.MarkdownService
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.resource.I18N
import org.jetbrains.kotlin.idea.base.util.module
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UIdentifier
import org.jetbrains.uast.UMethod


/**
 * 检查Api Method是否有对应的Markdown文件，并提供快速生成Markdown的方式[MarkdownNotExistsQuickFix]。
 *
 * @author iimik
 * @since 0.0.6
 **/
class MarkdownInspection : AbstractBaseUastLocalInspectionTool() {


    override fun checkMethod(method: UMethod, manager: InspectionManager, isOnTheFly: Boolean): Array<out ProblemDescriptor?>? {

        val apiMarker = service<ApiMethodService>().getApiMarker(method) ?: return null
        val module = method.module ?: return null
        val markdownFile = service<MarkdownService>().findMarkdownFile(module, apiMarker)

        if (markdownFile == null) {
            val problemDescriptor = manager.createProblemDescriptor(
                method.identifyingElement!!,
                I18N.message("Api.MarkdownInspection.descriptionTemplate"),
                MarkdownNotExistsQuickFix(module, apiMarker),
                ProblemHighlightType.WEAK_WARNING,
                isOnTheFly
            )
            return arrayOf(problemDescriptor)
        }


        return super.checkMethod(method, manager, isOnTheFly)
    }

}