package org.ifinalframework.plugins.aio.api.inspection;

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.components.service
import org.ifinalframework.plugins.aio.api.service.MarkdownService
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.psi.AbstractUastLocalInspectionTool
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
class MarkdownInspection : AbstractUastLocalInspectionTool() {

    override fun checkElement(element: UElement, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {

        if (element is UIdentifier) {
            val uastParent = element.uastParent
            if (uastParent is UMethod) {
                val psiElement = element.sourcePsi ?: return null

                val apiMarker = service<ApiMethodService>().getApiMarker(uastParent) ?: return null

                val markdownFile = service<MarkdownService>().findMarkdownFile(uastParent.module!!, apiMarker)

                if (markdownFile == null) {
                    val problemDescriptor = manager.createProblemDescriptor(
                        psiElement!!,
                        "Api Markdown for \"#ref\" not exists",
                        MarkdownNotExistsQuickFix(uastParent, apiMarker),
                        ProblemHighlightType.WEAK_WARNING,
                        isOnTheFly
                    )
                    return arrayOf(problemDescriptor)
                }
            }

        }

        return super.checkElement(element, manager, isOnTheFly)

    }
}