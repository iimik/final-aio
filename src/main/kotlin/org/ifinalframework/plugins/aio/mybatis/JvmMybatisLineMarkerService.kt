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

        // 排除默认方法
        if (parent is UMethod && parent.hasModifierProperty(PsiModifier.DEFAULT)) {
            return null
        }

        return when (parent) {
            is UClass -> MybatisMarker(element)
            is UMethod -> MybatisMarker(element)
            else -> null
        }

    }
}