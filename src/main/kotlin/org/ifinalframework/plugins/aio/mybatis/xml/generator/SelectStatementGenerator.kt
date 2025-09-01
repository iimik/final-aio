package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiJavaCodeReferenceElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiType
import com.intellij.psi.impl.source.PsiClassReferenceType
import org.ifinalframework.plugins.aio.datasource.model.Table
import org.ifinalframework.plugins.aio.mybatis.xml.MapperUtils
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Select
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Sql
import org.jetbrains.uast.UMethod


/**
 * Select Statement 生成器
 * - 支持自动填写`resultType`和`resultMap`属性
 *      - 方法的返回类型，如果是参数化类型，用只有一个参数（如List），收使用参数化类型，否则使用直接返回类型
 *      - 如果返回类型是基础类型（如String，Int等），则直接填写`resultType`属性，否则优化查询是否有定义`resultMap`，有则填写`resultMap`属性。
 *
 *
 *
 * ```xml
 * <select id="{method}" resultType="{returnType}" resultMap="{resultMap.id}">
 *     SELECT
 *     <include refId="{columns.sql.id}"/>
 *     FROM
 *     <include refId="{table.sql.id}"/>
 *     <where>
 *     </where>
 * </select>
 * ```
 *
 */
class SelectStatementGenerator : AbstractStatementGenerator<Select>() {
    override fun generateStatement(mapper: Mapper, method: UMethod, table: Table): Select {

        val returnType = resolveReturnType(method)
        val project = method.project

        return mapper.addSelect().apply {

            // 填写resultType或resultMap属性
            if (returnType is PsiPrimitiveType) {
                getResultType().stringValue = returnType.boxedTypeName
            } else if (returnType is PsiClassReferenceType) {
                val className = returnType.reference.qualifiedName

                val resultMap = mapper.getResultMaps().firstOrNull { it.getType().stringValue == className }
                if (resultMap != null) {
                    getResultMap().stringValue = resultMap.getId().stringValue
                } else {
                    getResultType().stringValue = className
                }

            }

            var tableSql = MapperUtils.getTableSql(project, mapper)
            var columnSql = MapperUtils.getColumnSql(project, mapper)
            if (tableSql == null) {
                tableSql = MapperUtils.createTableSql(project, mapper, table)
            }
            if (columnSql == null) {
                columnSql = MapperUtils.createColumnSql(project, mapper, table)
            }

            doGenerateSql(project, this, tableSql!!, columnSql!!)
        }

    }

    private fun doGenerateSql(project: Project, select: Select, tableSql: Sql, columnSql: Sql) {
        generateSql(
            project, select,
            "SELECT ",
            "<include refid=\"${tableSql!!.getId().stringValue}\"/>",
            "FROM ",
            "<include refid=\"${columnSql!!.getId().stringValue}\"/>"
        )

    }


    private fun resolveReturnType(method: PsiMethod): PsiType? {
        val psiType = method.returnType ?: return null

        if (psiType is PsiPrimitiveType) {
            return psiType
        }

        if (psiType is PsiClassReferenceType) {
            val reference = psiType.reference
            // 如果是参数类型且只有一个参数类型，如List<?>，取第一个元素，
            if (reference is PsiJavaCodeReferenceElement && reference.typeParameters.size == 1) {
                return reference.typeParameters[0]
            }
        }

        return psiType

    }

    override fun getDisplayText(): String {
        return "Select"
    }
}