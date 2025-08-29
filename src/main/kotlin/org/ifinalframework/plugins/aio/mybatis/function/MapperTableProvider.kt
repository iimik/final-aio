package org.ifinalframework.plugins.aio.mybatis.function

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper

/**
 * Mapper 表名提供者
 *
 * @author iimik
 */
interface MapperTableProvider {
    fun provide(project: Project, mapper: Mapper): String?
}

class DefaultMapperTableProvider : MapperTableProvider {

    private val providers = listOf<MapperTableProvider>(
        TableSqlFragmentMapperTableProvider()
    )

    override fun provide(
        project: Project,
        mapper: Mapper
    ): String? {
        for (provider in providers) {
            val table = provider.provide(project, mapper)
            if (!table.isNullOrBlank()) {
                return table.trim()
            }
        }

        return null
    }
}

private class TableSqlFragmentMapperTableProvider : MapperTableProvider {
    override fun provide(
        project: Project,
        mapper: Mapper
    ): String? {
        val myBatisProperties = project.service<MyBatisProperties>()
        val ids = myBatisProperties.tableSqlFragment.ids.split(",").toSet()
        val sql = mapper.getSqls().firstOrNull { ids.contains(it.getId().value) } ?: return null
        return sql.getValue()
    }
}