package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.database.model.basic.BasicTableColumn
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiJavaCodeReferenceElement
import com.intellij.psi.PsiType
import com.intellij.psi.PsiWildcardType
import com.intellij.psi.impl.source.PsiClassReferenceType
import org.ifinalframework.plugins.aio.datasource.model.Table
import org.ifinalframework.plugins.aio.mybatis.xml.MapperUtils
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Insert
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Sql
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Statement
import org.ifinalframework.plugins.aio.service.PsiService
import org.ifinalframework.plugins.aio.util.PsiTypeUtils
import org.jetbrains.uast.UMethod

/**
 *
 * #### 单个插入
 *
 * ```xml
 * <insert id="{insert}">
 *     INSERT INTO
 *     <include refid="table.sql.id"/>
 *     <trim prefix="(" suffix=")" suffixOverrides=",">
 *          <if test="property != null">
 *              `column`,
 *          </if>
 *     </trim>
 *     VALUES
 *     <trim prefix="(" suffix=")" suffixOverrides=",">
 *          <if test="property != null">
 *              #{property},
 *          </if>
 *     </trim>
 * </insert>
 * ```
 *
 * #### 批量插入
 *
 * ```xml
 * <insert id="{insert}">
 *     INSERT INTO
 *     <include refid="table.sql.id"/>
 *     <trim prefix="(" suffix=")" suffixOverrides=",">
 *         `column1`,
 *         `column2`,
 *         ...
 *     </trim>
 *     VALUES
 *     <foreach collection="list" item="item" separator=",">
 *         <trim prefix="(" suffix=")" suffixOverrides=",">
 *              #{item.property1},
 *              #{item.property2},
 *              ...
 *         </trim>
 *     </foreach>
 * </insert>
 * ```
 *
 */
class InsertStatementGenerator : AbstractStatementGenerator<Insert>() {
    override fun generateStatement(mapper: Mapper, method: UMethod, table: Table): Insert {
        return mapper.addInsert().apply {
            val project = method.project
            var tableSql = MapperUtils.getTableSql(project, mapper)

            if (tableSql == null) {
                tableSql = MapperUtils.createTableSql(project, mapper, table)
            }
            doGenerateSql(project, method, this, tableSql, table)

        }
    }

    private fun doGenerateSql(project: Project, method: UMethod, statement: Insert, tableSql: Sql, table: Table) {
        val parameters = method.parameterList.parameters
        if (parameters.size == 1) {
            val parameter = parameters[0]
            val isBatch = PsiTypeUtils.isList(parameter.type)
            val modelClass = resolveModelClass(project, parameter.type)
            if (isBatch) {
                generateBatchInsertSql(project, method, statement, tableSql, table, modelClass)
                processAutoIncTable(statement, table, "list")
            } else {
                generateInsertSql(project, method, statement, tableSql, table, modelClass)
            }
        } else {
            generateInsertSql(project, method, statement, tableSql, table, null)
        }


    }

    private fun processAutoIncTable(insert: Insert, table: Table, prefix: String?) {
        // 是否是自增
        val autoIncColumn = table.actualTables[0].columns.firstOrNull {
            when (it) {
                is BasicTableColumn -> it.isAutoInc
                else -> false
            }
        }
        if (autoIncColumn != null) {

            val keyProperty = if (prefix == null) autoIncColumn.name else "$prefix.${autoIncColumn.name}"

            insert.getUseGeneratedKeys().value = true
            insert.getKeyProperty().value = keyProperty
        }
    }

    private fun resolveModelClass(project: Project, psiType: PsiType): PsiClass? {
        if (psiType is PsiClassReferenceType) {
            val reference = psiType.reference
            if (reference is PsiJavaCodeReferenceElement && reference.typeParameters.size == 1) {
                return resolveModelClass(project, reference.typeParameters[0])
            }
            return project.service<PsiService>().findClass(reference.qualifiedName!!)
        }else if(psiType is PsiWildcardType){
            return resolveModelClass(project, psiType.bound!!)
        }

        return null
    }

    private fun generateInsertSql(
        project: Project,
        method: UMethod,
        statement: Insert,
        tableSql: Sql,
        table: Table,
        modelClass: PsiClass?
    ) {
        generateSql(
            project, statement,
            "INSERT INTO",
            "<include refid=\"${tableSql.getId().stringValue}\"/>",
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\"></trim>",
            "VALUES",
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\"></trim>"
        )
        tryGenerateColumnAndValue(project, method, statement, table, modelClass)
        processAutoIncTable(statement, table, null)

    }


    private fun generateBatchInsertSql(
        project: Project,
        method: UMethod,
        statement: Insert,
        tableSql: Sql,
        table: Table,
        modelClass: PsiClass?
    ) {

        generateSql(
            project, statement,
            "INSERT INTO",
            "<include refid=\"${tableSql.getId().stringValue}\"/>",
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\"></trim>",
            "VALUES",
            "<foreach collection=\"list\" item=\"item\" separator=\",\"></foreach>"
        )

        if (modelClass != null) {
            val columnsTrim = statement.getTrims()[0]
            val valuesTrim = statement.getForeachs()[0].addTrim().apply {
                getPrefix().stringValue = "("
                getSuffix().stringValue = ")"
                getSuffixOverrides().stringValue = ","
            }

            val fields = getInsertFields(modelClass, table)

            val columns = fields.joinToString("\n") { "`${generateColumn( it)}`," }
            val values = fields.joinToString("\n") { "#{item.${generateSqlParam( it)}}," }

            MapperUtils.appendSql(project, columnsTrim, columns)
            MapperUtils.appendSql(project, valuesTrim, values)

        }

    }

    /**
     * 找到需要插入的属性
     *
     * - 非自增属性
     */
    private fun getInsertFields(psiClass: PsiClass, table: Table): List<PsiField> {
        val autoIncColumns = table.actualTables[0].columns.filter {
            when (it) {
                is BasicTableColumn -> it.isAutoInc
                else -> false
            }
        }.map { it.name }.toSet()

        if (autoIncColumns.isEmpty()) {
            return psiClass.allFields.toList()
        }

        return psiClass.allFields.filter { !autoIncColumns.contains(generateColumn(it)) }.toList()

    }

    /**
     * 尝试生成列和值，仅支持当个参数列表
     */
    private fun tryGenerateColumnAndValue(project: Project, method: UMethod, statement: Statement, table: Table, modelClass: PsiClass?) {
        val trims = statement.getTrims()
        if (trims.size != 2) {
            return
        }

        val columnsTrim = trims[0]
        val valuesTrim = trims[1]

        if(modelClass == null){
            return
        }

        val insertFields = getInsertFields(modelClass, table)

        for (field in insertFields) {

            val test = generateTest(field)
            val column = generateColumn( field)
            val sqlParam = generateSqlParam(field)

            columnsTrim.addIf().apply {
                getTest().stringValue = test
                setValue("\n`$column`,\n")
            }

            valuesTrim.addIf().apply {
                getTest().stringValue = test
                setValue("\n#{$sqlParam},\n")
            }
        }


    }

    override fun getDisplayText(): String {
        return "Insert"
    }
}