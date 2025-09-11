package org.ifinalframework.plugins.aio.mybatis.inspection;

import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.components.service
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils
import org.ifinalframework.plugins.aio.mybatis.service.JvmMapperLineMarkerService
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.service.PsiService
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getContainingUClass


/**
 * Mapper 检查
 *
 * - 检查Mapper是否有定义xml文件
 *      - 区分是否存在有Method未定义`statement`
 *
 * - 检查Method是否有定义`statement`，忽略以下场景
 *      - 默认方法：含有`default`标记
 *      - 有特定注解：如`@Insert`
 * @issue 25
 * @author iimik
 * @since 0.0.4
 **/
class MapperInspection : AbstractBaseUastLocalInspectionTool() {

    /**
     * 检查是否有定义Mapper.xml文件
     * @issue 42
     * @since 0.0.10
     */
    override fun checkClass(uClass: UClass, manager: InspectionManager, isOnTheFly: Boolean): Array<out ProblemDescriptor?>? {
        val className = uClass.qualifiedName ?: return null
        if (!MyBatisUtils.isMapper(uClass)) return null
        val project = manager.project
        val mappers = project.service<MapperService>().findMappers(className)
        if (mappers.isNotEmpty()) return null

        // 检查是否有需要定义Statement的方法
        val mapperService = service<JvmMapperLineMarkerService>()
        val mapperClass = project.service<PsiService>().findClass(className)!!
        val findMethodButNotFoundStatement = mapperClass.methods.firstOrNull { MyBatisUtils.isStatementMethod(it) }

        val problemHighlightType = if (findMethodButNotFoundStatement == null) {
            ProblemHighlightType.WEAK_WARNING
        } else ProblemHighlightType.GENERIC_ERROR


        val problemDescriptor = manager.createProblemDescriptor(
            uClass.identifyingElement!!,
            "Mapper with namespaces=\"#ref\" not defined.",
            MapperNotExistsQuickFix(uClass),
            problemHighlightType,
            isOnTheFly
        )
        return arrayOf(problemDescriptor)
    }

    /**
     * 检查方法是否有定义statement
     */
    override fun checkMethod(method: UMethod, manager: InspectionManager, isOnTheFly: Boolean): Array<out ProblemDescriptor?>? {
        val sourcePsi = method.sourcePsi ?: return null

        val uClass = method.getContainingUClass() ?: return null

        if (!MyBatisUtils.isMapper(uClass)) return null
        val mapperService = service<JvmMapperLineMarkerService>()
        val marker = mapperService.apply(sourcePsi)

        if (marker != null && marker.targets == null) {

            val problemDescriptor = manager.createProblemDescriptor(
                method.identifyingElement!!,
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