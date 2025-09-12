package org.ifinalframework.plugins.aio.mybatis.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.common.util.getService
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils
import org.ifinalframework.plugins.aio.mybatis.MybatisMarker
import org.jetbrains.uast.*


/**
 * JvmMapperLineMarkerService
 *
 *
 * @author iimik
 * @since 0.0.4
 * @see [StatementLineMarkerService]
 **/
@Service
class JvmMapperLineMarkerService : MapperLineMarkerService<PsiElement> {

    override fun apply(element: PsiElement): MybatisMarker? {


        val uElement = when (element) {
            is UElement -> element
            is PsiElement -> element.toUElement()
            else -> null
        } ?: return null

        val myBatisProperties = element.project.service<MyBatisProperties>()

        val uClass = uElement.getContainingUClass() ?: return null

        // 以Mapper结尾的接口
        if (!uClass.isInterface || !uClass.name!!.endsWith("Mapper")) {
            return null
        }

        return when (uElement) {
            is UClass -> processClass(uElement)
            is UMethod -> if (myBatisProperties.lineMarker.mapperMethod) processMethod(uElement) else null
            else -> null
        }

    }

    private fun processClass(clazz: UClass): MybatisMarker? {
        val mappers = clazz.project.getService<MapperService>().findMappers()
        if (mappers.isEmpty()) return null

        val qualifiedName = clazz.qualifiedName
        val mapper = mappers.firstOrNull { qualifiedName == it.getNamespace().stringValue } ?: return MybatisMarker(null)

        return MybatisMarker(listOf(mapper.xmlTag!!))
    }

    private fun processMethod(method: UMethod): MybatisMarker? {

        if (!MyBatisUtils.isStatementMethod(method)) return null

        // 以下都不应该返回 null
        val mappers = method.project.getService<MapperService>().findMappers()
        if (mappers.isEmpty()) return null

        val qualifiedName = method.getContainingUClass()!!.qualifiedName
        val mapper = mappers.firstOrNull { qualifiedName == it.getNamespace()?.stringValue } ?: return MybatisMarker.NOT_EXISTS
        val statement = mapper.getStatements().firstOrNull { method.name == it.getId().stringValue } ?: return MybatisMarker.NOT_EXISTS

        return MybatisMarker(listOf(statement.xmlTag!!))
    }
}