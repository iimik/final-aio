package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.datasource.model.Table
import org.ifinalframework.plugins.aio.mybatis.xml.MapperUtils
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Delete
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Sql
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Statement
import org.jetbrains.uast.UMethod

/**
 * ```xml
 * <delete id="{delete}">
 *     DELETE FROM
 *     <include refid="table.sql.id"/>
 *     <where>
 *     </where>
 * </delete>
 * ```
 */
class DeleteStatementGenerator : AbstractStatementGenerator<Delete>() {
    override fun generateStatement(mapper: Mapper, method: UMethod, table: Table): Delete {
        val project = method.project
        return mapper.addDelete().apply {
            var tableSql = MapperUtils.getTableSql(project, mapper)

            if (tableSql == null) {
                tableSql = MapperUtils.createTableSql(project, mapper, table)
            }

            doGenerateSql(project, this, tableSql!!)

        }
    }

    private fun doGenerateSql(project: Project, statement: Statement, tableSql: Sql) {
        generateSql(
            project, statement,
            "DELETE FROM",
            "<include refid=\"${tableSql.getId().stringValue}\"/>",
        )
    }

    override fun getDisplayText(): String {
        return "Delete"
    }
}
