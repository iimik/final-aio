package org.ifinalframework.plugins.aio.mybatis.java;

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import org.ifinalframework.plugins.aio.application.condition.ConditionOnJava
import org.ifinalframework.plugins.aio.mybatis.MybatisLineMarkerService
import org.ifinalframework.plugins.aio.mybatis.MybatisMarker
import org.jetbrains.kotlin.nj2k.getContainingClass
import org.springframework.stereotype.Component


/**
 * JavaMybatisLineMarkerService
 * @issue 24
 * @author iimik
 * @since 0.0.4
 **/
@Component
@ConditionOnJava
class JavaMybatisLineMarkerService : MybatisLineMarkerService {
    override fun getMarker(element: PsiElement): MybatisMarker? {

        val psiClass = element.getContainingClass() ?: return null

        if(!psiClass.isInterface){
            return null
        }

        if(!psiClass.name!!.endsWith("Mapper")){
            return null
        }

        return if (element is PsiIdentifier && element.parent is PsiClass) {
            MybatisMarker(element)
        } else null
    }
}