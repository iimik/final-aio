package org.ifinalframework.plugins.aio.mybatis.inspection;

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils
import org.ifinalframework.plugins.aio.mybatis.service.JvmMapperLineMarkerService
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.psi.AbstractUastLocalInspectionTool
import org.jetbrains.uast.*


/**
 * Mapper 检查
 *
 * - 检查Mapper是否有定义xml文件
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

        if (element !is UIdentifier) return null

        val sourcePsi = element.sourcePsi ?: return null
        val parent = element.uastParent ?: return null

        val uClass = element.getContainingUClass() ?: return null

        if (!MyBatisUtils.isMapper(uClass)) return null

        return when (parent) {
            is UClass -> checkClass(sourcePsi, parent, manager, isOnTheFly)
            is UMethod -> checkMethod(sourcePsi, parent, manager, isOnTheFly)
            else -> null
        }

    }


    /**
     * 检查是否有定义Mapper.xml文件
     * @issue 42
     * @since 0.0.10
     */
    private fun checkClass(
        sourcePsi: PsiElement,
        clazz: UClass,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor>? {
        val className = clazz.qualifiedName ?: return null
        val mappers = manager.project.service<MapperService>().findMappers(className)
        if (mappers.isNotEmpty()) return null

        val problemDescriptor = manager.createProblemDescriptor(
            sourcePsi,
            "Mapper with namespaces=\"#ref\" not defined.",
            MapperNotExistsQuickFix(clazz),
            ProblemHighlightType.GENERIC_ERROR,
            isOnTheFly
        )
        return arrayOf(problemDescriptor)
    }

    private fun checkMethod(
        sourcePsi: PsiElement,
        method: UMethod,
        manager: InspectionManager,
        isOnTheFly: Boolean
    ): Array<ProblemDescriptor>? {

        val mapperService = service<JvmMapperLineMarkerService>()
        val marker = mapperService.apply(sourcePsi)

        if (marker != null && marker.targets == null) {

            val problemDescriptor = manager.createProblemDescriptor(
                sourcePsi!!,
                "Statement with id=\"#ref\" not defined in mapper XML",
                StatementNotExistsQuickFix(method),
                ProblemHighlightType.GENERIC_ERROR,
                isOnTheFly
            )
            return arrayOf(problemDescriptor)


        }

        return null
    }


}