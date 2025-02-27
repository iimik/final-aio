package org.ifinalframework.plugins.aio.mybatis.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.xml.DomService
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.mybatis.xml.dom.ResultMap


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

}