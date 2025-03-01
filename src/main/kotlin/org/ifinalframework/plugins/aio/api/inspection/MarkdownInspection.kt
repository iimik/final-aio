package org.ifinalframework.plugins.aio.api.inspection;

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.components.service
import org.ifinalframework.plugins.aio.api.service.MarkdownService
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.mybatis.inspection.StatementNotExistsQuickFix
import org.ifinalframework.plugins.aio.psi.AbstractUastLocalInspectionTool
import org.jetbrains.kotlin.idea.base.util.module
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UIdentifier
import org.jetbrains.uast.UMethod


/**
 * MarkdownInspection
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
                        StatementNotExistsQuickFix(uastParent),
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