package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils
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
    override fun generateStatement(mapper: Mapper, method: UMethod): Update {
        val project = method.project
        return mapper.addUpdate().apply {
            var tableSql = MapperUtils.getTableSql(project, mapper)

            if (tableSql == null) {

                val tables = MapperUtils.getMapperTableOptions(project, mapper)

                if (tables.isEmpty()) {
                    return@apply
                }
                if (tables.size == 1) {
                    val table = tables[0]
                    if (tableSql == null) {
                        tableSql = MapperUtils.createTableSql(project, mapper, table)
                    }

                    doGenerateSql(project, this, tableSql)
                } else if (tables.size > 1) {
                    MyBatisUtils.showTableSelectPopup("请选择${mapper.getNamespace().value!!.name}对应的数据表", tables) { table ->
                        WriteCommandAction.runWriteCommandAction(project) {
                            if (tableSql == null) {
                                tableSql = MapperUtils.createTableSql(project, mapper, table)
                            }

                            doGenerateSql(project, this, tableSql!!)
                        }

                    }
                }


            } else {
                doGenerateSql(project, this, tableSql!!)
            }

        }
    }

    private fun doGenerateSql(project: Project, statement: Statement, tableSql: Sql) {
        generateSql(
            project, statement,
            "UPDATE ",
            "<include refid=\"${tableSql.getId().stringValue}\"/>",
            "<set></set>",
            "<where></where>"
        )
    }

    override fun getDisplayText(): String {
        return "Update"
    }
}
