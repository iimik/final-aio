package org.ifinalframework.plugins.aio.mybatis;

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifier
import org.ifinalframework.plugins.aio.application.condition.ConditionOnJvm
import org.jetbrains.uast.*
import org.springframework.stereotype.Component


/**
 * JvmMybatisLineMarkerService
 *
 * - 接口
 * - 非`default`方法
 * - 未被特定注解标记，如`@Insert`、`@InsertProvider`等。
 *
 * @author iimik
 * @since 0.0.4
 **/
@Component
@ConditionOnJvm
class JvmMybatisLineMarkerService : MybatisLineMarkerService {
    override fun getMarker(element: PsiElement): MybatisMarker? {
        val uElement = element.toUElement() ?: return null

        val uClass = uElement.getContainingUClass() ?: return null

        // 以Mapper结尾的接口
        if (!uClass.isInterface || !uClass.name!!.endsWith("Mapper")) {
            return null
        }

        if (uElement !is UIdentifier) return null

        val parent = uElement.uastParent ?: return null

        if (parent is UMethod) {

            // 排除默认方法
            if (parent.hasModifierProperty(PsiModifier.DEFAULT)) {
                return null
            }
            // 含有特定注解
            MybatisAnnotations.ALL_STATEMENTS.map { parent.hasAnnotation(it) }.firstOrNull { it }?.let { return null }

        }

        return when (parent) {
            is UClass -> MybatisMarker(element)
            is UMethod -> MybatisMarker(element)
            else -> null
        }

    }
}