package org.ifinalframework.plugins.aio.mybatis.service;

import com.intellij.grazie.utils.orFalse
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifier
import org.ifinalframework.plugins.aio.mybatis.MybatisConstants
import org.ifinalframework.plugins.aio.mybatis.MybatisMarker
import org.jetbrains.uast.*


/**
 * JvmMapperLineMarkerService
 *
 * @author iimik
 * @since 0.0.4
 **/
@Service(Service.Level.PROJECT)
class JvmMapperLineMarkerService(
    project: Project
) : MapperLineMarkerService<Any> {

    private val mapperService: MapperService = project.getService(MapperService::class.java)

    override fun apply(element: Any): MybatisMarker? {
        val uElement = when (element) {
            is UElement -> element
            is PsiElement -> element.toUElement()
            else -> null
        } ?: return null

        val uClass = uElement.getContainingUClass() ?: return null

        // 以Mapper结尾的接口
        if (!uClass.isInterface || !uClass.name!!.endsWith("Mapper")) {
            return null
        }

        if (uElement !is UIdentifier) return null

        val parent = uElement.uastParent ?: return null

        return when (parent) {
            is UClass -> processClass(parent)
            is UMethod -> processMethod(parent)
            else -> null
        }

    }

    private fun processClass(clazz: UClass): MybatisMarker? {
        val mappers = mapperService.findMappers()
        if (mappers.isEmpty()) return null

        val qualifiedName = clazz.qualifiedName
        val mapper = mappers.firstOrNull { qualifiedName == it.getNamespace().stringValue } ?: return MybatisMarker(null)

        return MybatisMarker(listOf(mapper?.xmlTag!!))
    }

    private fun processMethod(method: UMethod): MybatisMarker? {

        // 排除默认方法
        if (method.hasModifierProperty(PsiModifier.DEFAULT)) {
            return null
        }
        // 含有特定注解
        val hasAnnotation = MybatisConstants.ALL_STATEMENTS.map { method.hasAnnotation(it) }.firstOrNull { it }.orFalse()
        if (hasAnnotation) {
            return null
        }

        // 以下都不应该返回 null

        val mappers = mapperService.findMappers()
        if (mappers.isEmpty()) return null

        val qualifiedName = method.getContainingUClass()!!.qualifiedName
        val mapper = mappers.firstOrNull { qualifiedName == it.getNamespace()?.value } ?: return MybatisMarker.NOT_EXISTS
        val statement = mapper.getStatements().firstOrNull { method.name == it.getId().value } ?: return MybatisMarker.NOT_EXISTS

        return MybatisMarker(listOf(statement.xmlTag!!))
    }
}