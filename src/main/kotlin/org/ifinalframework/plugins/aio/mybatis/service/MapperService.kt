package org.ifinalframework.plugins.aio.mybatis.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.xml.DomService
import org.ifinalframework.plugins.aio.mybatis.MapperUtils
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.mybatis.xml.dom.ResultMap
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Statement
import org.ifinalframework.plugins.aio.service.PsiService


/**
 * MapperService
 *
 * @author iimik
 * @since 0.0.4
 **/
@Service(Service.Level.PROJECT)
class MapperService(
    private val project: Project,
) {

    fun findMappers(): Collection<Mapper> {
        return DomService.getInstance().getFileElements(Mapper::class.java, project, GlobalSearchScope.allScope(project))
            .map { it.rootElement }
    }

    fun findResultMaps(className: String): Collection<ResultMap> {
        return findMappers().flatMap { it.getResultMaps() }
            .filter { className == it.getType().stringValue }
            .toList()
    }

    fun findMethod(className: String, methodName: String): PsiMethod? {
        return findMethod(null, className, methodName)
    }

    fun findMethod(statement: Statement): PsiMethod? {
        val className = MapperUtils.getNamespace(statement)
        val methodName = statement.getId().rawText ?: return null
        return findMethod(className, methodName)
    }

    fun findMethod(module: Module?, className: String, methodName: String): PsiMethod? {
        val clazz = project.service<PsiService>().findClass(className) ?: return null
        val methods = clazz.findMethodsByName(methodName, true)
        if (methods.isEmpty()) return null
        return methods.firstOrNull { MapperUtils.isStatementMethod(it) }
    }

    fun findMethods(statement: Statement): List<PsiMethod> {
        val className = MapperUtils.getNamespace(statement)
        val methodName = statement.getId().rawText ?: return emptyList()
        return findMethods(className, methodName)
    }

    fun findMethods(className: String, methodName: String): List<PsiMethod> {
        val clazz = project.service<PsiService>().findClass(className) ?: return emptyList()
        val methods = clazz.findMethodsByName(methodName, true)
        if (methods.isEmpty()) return emptyList()
        return methods.filter { MapperUtils.isStatementMethod(it) }
    }

}