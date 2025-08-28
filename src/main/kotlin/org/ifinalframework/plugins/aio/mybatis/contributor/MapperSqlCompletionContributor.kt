package org.ifinalframework.plugins.aio.mybatis.contributor

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.util.PlatformIcons
import com.intellij.util.xml.DomUtil
import org.ifinalframework.plugins.aio.datasource.service.DataSourceService
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Sql
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.service.PsiService

/**
 * SQL 参数补全
 *
 * 支持以下类型的补全
 * - 表名补全：`<sql>`标签的`id`属性应被配置[MyBatisProperties.TableSqlFragment.ids]包含
 * - 所有列补全：`<sql>`标签的`id`属性应被配置[MyBatisProperties.ColumnSqlFragment.ids]包含
 * - SQL参数补全：内容被`${}`或`#{}`包裹
 *
 * @issue 32
 * @author iimik
 * @since 0.0.7
 */
class MapperSqlCompletionContributor : AbsMapperCompletionContributor() {
    val docService = service<DocService>()
    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        if (parameters.completionType != CompletionType.BASIC) {
            return
        }

        val position = parameters.position
        val context = position.containingFile.context
        val topLevelFile = context?.containingFile ?: return
        if (MyBatisUtils.isMapperXml(topLevelFile)) {
            val myBatisProperties = position.project.service<MyBatisProperties>()

            val domElement = DomUtil.getDomElement(context)
            if (domElement is Sql) {
                if (myBatisProperties.tableSqlFragment.ids.split(",").contains(domElement.getId().value)) {
                    // 是Table Sql Fragment
                    processTableSqlFragment(domElement, parameters, result)
                }
                if (myBatisProperties.columnSqlFragment.ids.split(",").contains(domElement.getId().value)) {
                    // 是Column Sql Fragment
                    processColumnSqlFragment(domElement, parameters, result)
                }

            }

            val shouldAddElement = shouldAddElement(position.containingFile, parameters.offset)
            if (shouldAddElement) {
                process(topLevelFile, result, position)
            }
        }
    }

    /**
     * 填充表
     * ```xml
     * <sql id="table">
     *     t_table
     * </sql>
     * ```
     */
    private fun processTableSqlFragment(sql: Sql, parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position
        val myBatisProperties = position.project.service<MyBatisProperties>()
        val dataSourceService = position.project.service<DataSourceService>()
        val tables = dataSourceService.getTables(myBatisProperties.tableSqlFragment.prefix)
        if (tables.isNotEmpty()) {
            tables.forEach { table ->
                result.addElement(
                    LookupElementBuilder.create(table.logicTable)
                        .withTypeText(table.toString())
                        .withCaseSensitivity(false)
                )
            }
            result.stopHere()
        }
    }

    /**
     * 填充列
     * ```xml
     * <sql id="columns">
     *     column1, column2, ...
     * </sql>
     * ```
     */
    private fun processColumnSqlFragment(sql: Sql, parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position
        val myBatisProperties = position.project.service<MyBatisProperties>()
        val dataSourceService = position.project.service<DataSourceService>()
        val tables = dataSourceService.getTables(myBatisProperties.tableSqlFragment.prefix)
        if (tables.isEmpty()) {
            return
        }



        val tableSqlFragmentIds = myBatisProperties.tableSqlFragment.ids.split(",")
        val mapper = DomUtil.getParentOfType(sql, Mapper::class.java, true) ?: return
        val tableSqls = mapper.getSqls().filter { tableSqlFragmentIds.contains(it.getId().value) }.toList()
        if (tableSqls.isEmpty()) {
            return
        }

        val tableName = tableSqls.get(0).getValue()
        if (tableName.isNullOrBlank()) {
            return
        }

        val matchTables = tables.filter { tableName == it.logicTable }.toList()
        if (matchTables.isNotEmpty()) {
            // 找到了匹配的表
            val table = matchTables.get(0).actualTables[0]
            val columns = table.columns.map { "`${it.name}`" }.joinToString(", ")
            result.addElement(
                LookupElementBuilder.create(columns)
                    .withTypeText("${table.name}(${table.comment})")
                    .withCaseSensitivity(false)
            )
            result.stopHere()
        }

    }

    private fun process(xmlFile: PsiFile, result: CompletionResultSet, position: PsiElement) {
        val psiFile = position.containingFile
        val context = position.containingFile.context ?: return
        val statement = MyBatisUtils.findStatementDomElement(context) ?: return

        val method = statement.getId().value ?: return
        val params = method.parameterList.parameters
        if (params.size == 1) {
            val type = params[0].type
            processParam(null, type, result, position.project)
        } else {
            params.forEach { param ->
                val prefix = param.name
                val type = param.type
                processParam(prefix, type, result, position.project)
            }
        }

        result.stopHere()

    }


    private fun processParam(prefix: String?, type: PsiType, result: CompletionResultSet, project: Project) {

        if (type is PsiClassReferenceType) {
            val className = type.reference.qualifiedName
            if (className.startsWith("java.lang.") || className.startsWith("java.util.")) {
                val name = prefix ?: "value"
                result.addElement(
                    LookupElementBuilder.create(name)
                        .withIcon(PlatformIcons.PARAMETER_ICON)
                        .withCaseSensitivity(false)
                )
            } else {
                val clazz = project.service<PsiService>().findClass(className) ?: return
                clazz.allFields
                    .filter { !it.hasModifierProperty(PsiModifier.STATIC) }
                    .forEach { field ->
                        var typeText = field.type.presentableText

                        docService.getSummary(field)?.let { typeText = "$it ($typeText)" }
                        val typeHandler = docService.findTagValueByTag(field, "typeHandler")
                        var name = field.name

                        if (typeHandler != null) {
                            name = "$name,typeHandler=$typeHandler"
                        }

                        result.addElement(
                            LookupElementBuilder.create(name)
                                .withIcon(PlatformIcons.PROPERTY_ICON)
                                .withTypeText(typeText)
                                .withCaseSensitivity(false)
                        )

                    }
            }
        } else if (type is PsiPrimitiveType) {
            val name = prefix ?: "value"
            result.addElement(
                LookupElementBuilder.create(name)
                    .withIcon(PlatformIcons.PARAMETER_ICON)
                    .withCaseSensitivity(false)
            )
        }

    }

    private fun shouldAddElement(file: PsiFile, offset: Int): Boolean {
        val text = file.text
        for (i in offset - 1 downTo 1) {
            val c = text[i]
            if (c == '{' && (text[i - 1] == '#' || text[i - 1] == '$')) return true
        }
        return false
    }
}