package org.ifinalframework.plugins.aio.mybatis.xml.generator

import org.ifinalframework.plugins.aio.datasource.model.Table
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Delete
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
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
        return mapper.addDelete()
    }

    override fun getDisplayText(): String {
        return "Delete"
    }
}
