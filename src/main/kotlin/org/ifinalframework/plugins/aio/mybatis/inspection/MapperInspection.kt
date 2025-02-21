package org.ifinalframework.plugins.aio.mybatis.inspection;

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import org.ifinalframework.plugins.aio.mybatis.service.JvmMapperLineMarkerService
import org.ifinalframework.plugins.aio.psi.AbstractUastLocalInspectionTool
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UIdentifier
import org.jetbrains.uast.UMethod


/**
 * Mapper 检查
 *
 * - 检查Method是否有定义`statement`，忽略以下场景
 *      - 默认方法：含有`default`标记
 *      - 有特定注解：如`@Insert`
 * @issue 25
 * @author iimik
 * @since 0.0.4
 **/
class MapperInspection : AbstractUastLocalInspectionTool() {

    override fun checkElement(element: UElement, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        if (element is UIdentifier) {

            val parent = element.uastParent ?: return null

            val psiElement = element.sourcePsi!!
            val project = psiElement.project
            val mapperService = project.getService(JvmMapperLineMarkerService::class.java)
            val marker = mapperService.apply(element)

            if (marker != null && marker.targets == null) {

                return if (parent is UMethod) {
                    val problemDescriptor = manager.createProblemDescriptor(
                        psiElement!!,
                        "Statement with id=\"#ref\" not defined in mapper XML",
                        StatementNotExistsQuickFix(parent),
                        ProblemHighlightType.GENERIC_ERROR,
                        isOnTheFly
                    )
                    arrayOf(problemDescriptor)
                } else null

            }
        }
        return null
    }


}