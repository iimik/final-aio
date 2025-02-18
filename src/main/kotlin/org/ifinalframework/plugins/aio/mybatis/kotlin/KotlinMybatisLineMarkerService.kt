package org.ifinalframework.plugins.aio.mybatis.kotlin;

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.application.condition.ConditionOnKotlin
import org.ifinalframework.plugins.aio.mybatis.MybatisLineMarkerService
import org.ifinalframework.plugins.aio.mybatis.MybatisMarker
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.psiUtil.containingClass
import org.springframework.stereotype.Component


/**
 * KotlinMybatisLineMarkerService
 *
 * @author iimik
 * @since 0.0.4
 **/
@Component
@ConditionOnKotlin
class KotlinMybatisLineMarkerService : MybatisLineMarkerService {
    override fun getMarker(element: PsiElement): MybatisMarker? {
        if (element !is KtElement) return null
        val ktClass = element.containingClass() ?: return null
        if (!ktClass.isInterface()) return null
        if (!ktClass.name!!.endsWith("Mapper")) return null

        return when (element) {
            is KtClass -> MybatisMarker(element)
            is KtClassBody -> MybatisMarker(element)
            else -> null
        }
    }
}