package org.ifinalframework.plugins.aio.mybatis.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiEnumConstant
import org.ifinalframework.plugins.aio.service.PsiService
import org.jetbrains.uast.UClass
import org.jetbrains.uast.toUElement

/**
 * MybatisService
 *
 * @author iimik
 */
@Service(Service.Level.PROJECT)
class MybatisService(
    private val project: Project,
) {

    private val typeHandler: PsiClass? = project.service<PsiService>().findClass("org.apache.ibatis.type.TypeHandler")

    fun isTypeHandler(clazz: PsiElement): Boolean {

        if (typeHandler == null) {
            return false
        }

        val uElement = clazz.toUElement() ?: return false
        return if (uElement is UClass) {
            uElement.isInheritor(typeHandler, true)
        } else false
    }

    fun getJdbcTypes(): List<PsiEnumConstant> {
        val jdbcTypeClass = project.service<PsiService>().findClass("org.apache.ibatis.type.JdbcType") ?: return emptyList()

        return jdbcTypeClass.allFields
            .filterIsInstance<PsiEnumConstant>()
    }

    fun getJdbcType(name: String): PsiEnumConstant? {
        return getJdbcTypes().find { it.name == name }
    }

}