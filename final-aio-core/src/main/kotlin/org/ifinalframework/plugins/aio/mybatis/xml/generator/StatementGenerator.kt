package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.datasource.model.Table
import org.jetbrains.uast.UMethod

/**
 * Statement 生成器
 * - Insert: [InsertStatementGenerator]
 * - Update: [UpdateStatementGenerator]
 * - Delete: [DeleteStatementGenerator]
 * - Select: [SelectStatementGenerator]
 * @author iimik
 * @see [org.ifinalframework.plugins.aio.mybatis.xml.dom.Statement]
 */
interface StatementGenerator {
    fun generate(project: Project, method: UMethod, table: Table)
}