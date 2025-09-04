package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.codeInsight.navigation.activateFileWithPsiElement
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.xml.XmlTag
import org.ifinalframework.plugins.aio.datasource.model.Table
import org.ifinalframework.plugins.aio.jvm.AnnotationService
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.MybatisConstants
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Insert
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Statement
import org.ifinalframework.plugins.aio.mybatis.xml.model.AndOr
import org.ifinalframework.plugins.aio.mybatis.xml.model.CriterionType
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.service.PsiService
import org.ifinalframework.plugins.aio.util.CaseFormatUtils
import org.ifinalframework.plugins.aio.util.XmlUtils
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getContainingUClass
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

abstract class AbstractStatementGenerator<T : Statement> : StatementGenerator {

    private val TEST_COMPLETION_PLACE_HOLDER = "\${TARGET}"
    private val TEST_COMPLETION_START_PLACE_HOLDER = "\${START_TARGET}"
    private val TEST_COMPLETION_END_PLACE_HOLDER = "\${END_TARGET}"


    private val docService = service<DocService>()
    private val annotationService = service<AnnotationService>()

    final override fun generate(project: Project, method: UMethod, table: Table) {
        val uClass = method.getContainingUClass() ?: return
        val mappers = project.service<MapperService>().findMappers(uClass.qualifiedName!!)
        if (mappers.isEmpty()) {
            // mapper 不存在，还需要创建mapper
        } else if (mappers.size == 1) {
            // 只有一个mapper，直接添加
            val mapper = mappers.iterator().next()
            val statement = generateStatement(mapper, method, table).apply {
                getId().stringValue = method.name
            }

            if (statement !is Insert) {
                generateWhere(statement, method, table)
            }

            val tag: XmlTag = statement.xmlTag!!
            val offset = tag.textOffset + tag.textLength - tag.name.length - 3
            val editorService: FileEditorManager = FileEditorManager.getInstance(project)
            activateFileWithPsiElement(tag, true)
//                editorService.openFile(tag.containingFile!!.virtualFile)
            val editor = editorService.selectedTextEditor
            if (null != editor) {
                editor.caretModel.moveToOffset(offset)
                editor.scrollingModel.scrollToCaret(ScrollType.RELATIVE)
            }

        } else {

        }
    }


    protected abstract fun generateStatement(mapper: Mapper, method: UMethod, table: Table): T

    protected fun generateSql(project: Project, statement: Statement, vararg sqls: String) {
        var addedElement: PsiElement? = null

        sqls.forEach { content ->
            val element = XmlUtils.createElement(project, content)
            addedElement = if (addedElement == null) {
                statement.xmlTag!!.add(element)
            } else {
                statement.xmlTag!!.addAfter(element, addedElement)
            }
        }
    }


    protected fun generateColumn(psiElement: PsiElement): String {
        val column = docService.findTagValueByTag(psiElement, "column")

        if (column != null && column.isNotEmpty()) {
            return column;
        }

        val name = getName(psiElement)

        return when (name) {
            "id" -> "id"
            "ids" -> "id"
            else -> CaseFormatUtils.lowerCamel2LowerUnderscore(name)
        } as String

    }

    protected fun generateSqlParam(psiElement: PsiElement): String {
        val name = getName(psiElement)

        val typeHandler = docService.findTagValueByTag(psiElement, "typeHandler") ?: return name

        return "$name, typeHandler=$typeHandler"
    }

    protected fun generateTest(psiElement: PsiElement, prefix: String? = null): String {
        val project = psiElement.project
        val name = getName(psiElement)
        val psiType = getType(psiElement)
        val myBatisProperties = project.service<MyBatisProperties>()
        val testCompletion = myBatisProperties.testCompletion
        val param = Stream.of(prefix, name).filter { Objects.nonNull(it) }.collect(Collectors.joining("."))

        // TODO 当param中有.时，需要判断引用是否null

        if (psiType is PsiClassReferenceType) {
            val className = psiType.reference.qualifiedName
            if ("java.lang.String" == className) {
                return testCompletion.stringType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
            } else if ("java.util.List" == className || "java.util.Set" == className) {
                return testCompletion.collectionType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
            }
        }

        return testCompletion.defaultType.replace(TEST_COMPLETION_PLACE_HOLDER, param)
    }

    /**
     * - 只有一个参数
     *      - `id`: 参数名叫ID， `id = #{id}`
     *      - `ids`: 参数名叫ids， `id in (#{ids.item})`
     *      - 其他参数： <if test="property !null">AND column = #{property}</if>
     * - 多个参数
     *
     */
    protected fun generateWhere(statement: Statement, method: UMethod, table: Table) {

        val parameters = method.parameterList.parameters
        if (parameters.isEmpty()) {
            // 没有参数，不需要处理
            return
        }

        val wheres = statement.getWheres()

        val list = mutableListOf<String>()

        for (parameter in parameters) {
            doGenerateWhere(list, parameter, parameters.size == 1)

            if (list.isEmpty()) {
                return
            }

            if (wheres.isEmpty()) {
                var where = list.joinToString("\n")
                if (where.startsWith("and ", ignoreCase = true) || where.startsWith("or ", ignoreCase = true)) {
                    where = where.substringAfter(" ")
                }
                val element = XmlUtils.createElement(method.project, "<where>\n$where\n</where>")
                statement.xmlTag!!.add(element)
            } else {
                // TODO
            }

        }

    }


    private fun doGenerateWhere(where: MutableList<String>, parameter: PsiParameter, isSingleParam: Boolean) {
        val paramAnnotation = annotationService.findAnnotationAttributes(parameter, MybatisConstants.PARAM)
        val prefix: String? = (if (paramAnnotation != null) paramAnnotation["value"] else null) as String?
        val name = getName(parameter)
        val type = getType(parameter)
        if ("id" == name) {
            // where id = #{id}
            doGenerateWhereCriterion(where, parameter, null, AndOr.AND, CriterionType.EQUAL, true)
        } else if ("ids" == name) {
            // where ids in #{ids.item}
            doGenerateWhereCriterion(where, parameter, null, AndOr.AND, CriterionType.IN, true)
        } else {
            if (type is PsiClassReferenceType) {
                val qualifiedName = type.reference.qualifiedName ?: return

                if (qualifiedName == "java.lang.String") {
                    doGenerateWhereCriterion(where, parameter, null, AndOr.AND, CriterionType.EQUAL, false)
                }

                if (qualifiedName.startsWith("java.")) {
                    return
                }
                val psiClass = parameter.project.service<PsiService>().findClass(qualifiedName) ?: return
                for (psiField in psiClass.allFields) {
                    if (psiField.hasModifierProperty(PsiModifier.STATIC)) {
                        continue
                    }

                    doGenerateWhereCriterion(where, psiField, prefix, AndOr.AND, CriterionType.EQUAL, false)

                }
            }
        }
    }

    private fun doGenerateWhereCriterion(
        where: MutableList<String>,
        psiElement: PsiElement,
        prefix: String?,
        andOr: AndOr,
        criterionType: CriterionType,
        required: Boolean
    ) {

        when (criterionType) {
            CriterionType.IN -> doGenerateWhereInCriterion(where, psiElement, prefix, andOr, criterionType, required)
            CriterionType.NOT_INT -> doGenerateWhereInCriterion(where, psiElement, prefix, andOr, criterionType, required)
            else -> doGenerateWhereSimpleCriterion(where, psiElement, prefix, andOr, criterionType, required)
        }

    }

    private fun doGenerateWhereSimpleCriterion(
        where: MutableList<String>,
        psiElement: PsiElement,
        prefix: String?,
        andOr: AndOr,
        criterionType: CriterionType,

        required: Boolean
    ) {

        val test = generateTest(psiElement, prefix)
        val column = generateColumn(psiElement)
        val sqlParam = generateSqlParam(psiElement)
        val param = Stream.of<String>(prefix, sqlParam).filter { Objects.nonNull(it) }.collect(Collectors.joining("."))

        if (required) {
            where.add("$andOr $column ${criterionType.operation} #{$param}")
        } else {
            where.add(
                """
              <if test="$test">
                $andOr $column ${criterionType.operation} #{$param}
              </if>          
        """.trimIndent()
            )
        }


    }

    private fun doGenerateWhereInCriterion(
        where: MutableList<String>,
        psiElement: PsiElement,
        prefix: String?,
        andOr: AndOr,
        criterionType: CriterionType,
        required: Boolean
    ) {
        val test = generateTest(psiElement, prefix)
        val name = getName(psiElement)
        val column = generateColumn(psiElement)
        val collection = Stream.of<String>(prefix, name).filter { Objects.nonNull(it) }.collect(Collectors.joining("."))


        if (required) {
            // 不需要<if test>
            where.add("$andOr $column ${criterionType.operation}")
            where.add(
                """
                <foreach collection="$collection" item="item" open="(" close=")" separator=",">
                    #{item}
                </foreach>
            """.trimIndent()
            )
        } else {
            where.add(
                """
            <if test="$test">
                $andOr $column ${criterionType.operation}
                <foreach collection="$collection" item="item" open="(" close=")" separator=",">
                    #{item}
                </foreach>
            </if>
        """.trimIndent()
            )
        }

    }


    private fun getName(psiElement: PsiElement): String {
        return when (psiElement) {
            is PsiField -> psiElement.name
            is PsiParameter -> psiElement.name
            else -> throw IllegalArgumentException("${psiElement.javaClass} is not a field")
        }
    }


    private fun getType(psiElement: PsiElement): PsiType? {
        return when (psiElement) {
            is PsiField -> psiElement.type
            is PsiParameter -> psiElement.type
            else -> throw IllegalArgumentException("${psiElement.javaClass} is not a field")
        }
    }

    protected abstract fun getDisplayText(): String

    override fun toString(): String {
        return getDisplayText()
    }
}
