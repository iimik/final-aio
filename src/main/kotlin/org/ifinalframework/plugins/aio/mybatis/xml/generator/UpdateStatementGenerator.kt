package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.datasource.model.Table
import org.ifinalframework.plugins.aio.mybatis.xml.MapperUtils
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Sql
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Statement
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Update
import org.jetbrains.uast.UMethod

/**
 * ```xml
 * <update id="{update}">
 *     UPDATE
 *     <include refid="{table.sql.id}"/>
 *     <set>
 *     </set>
 *     <where>
 *     </where>
 * </update>
 * ```
 */
class UpdateStatementGenerator : AbstractStatementGenerator<Update>() {
    override fun generateStatement(mapper: Mapper, method: UMethod, table: Table): Update {
        val project = method.project
        return mapper.addUpdate().apply {
            var tableSql = MapperUtils.generateTableSqlIfNotExists(project, mapper, table)
            doGenerateSql(project, this, tableSql!!)

        }
    }

    private fun doGenerateSql(project: Project, statement: Statement, tableSql: Sql) {
        generateSql(
            project, statement,
            "UPDATE ",
            "<include refid=\"${tableSql.getId().stringValue}\"/>",
            "<set></set>"
        )
    }

    override fun getDisplayText(): String {
        return "Update"
    }
}
