package org.ifinalframework.plugins.aio.mybatis.inspection;

import com.intellij.codeInsight.navigation.activateFileWithPsiElement
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.psi.*
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.xml.XmlTag
import org.ifinalframework.plugins.aio.intellij.GenericQuickFix
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.mybatis.xml.MapperUtils
import org.ifinalframework.plugins.aio.mybatis.xml.dom.*
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.ifinalframework.plugins.aio.util.XmlUtils
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getContainingUClass
import java.util.stream.Collectors
import java.util.stream.Stream


/**
 * StatementNotExistsQuickFix
 *
 * @issue 43
 * @author iimik
 * @since 0.0.10
 **/
class StatementNotExistsQuickFix(val method: UMethod) : GenericQuickFix() {

    private val statementGenerators: Map<String, StatementGenerator> = listOf(
        InsertStatementGenerator(),
        UpdateStatementGenerator(),
        DeleteStatementGenerator(),
        SelectStatementGenerator()
    ).stream().collect(Collectors.toMap({ it.toString().lowercase() }, { it }))


    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        method.getContainingUClass() ?: return


        val generators = findStatementGenerators(project)

        if (generators.size == 1) {
            val generator = generators.first()
            generator.generate(project, method)
        } else {
            JBPopupFactory.getInstance().createListPopup(
                object : BaseListPopupStep<StatementGenerator>(
                    "[ Statement type for method: " + method.name + "]",
                    generators,
                    AllIcons.Mybatis.JVM
                ) {
                    override fun onChosen(selectedValue: StatementGenerator, finalChoice: Boolean): PopupStep<*>? {
                        WriteCommandAction.runWriteCommandAction(project) {
                            selectedValue.generate(project, method)
                        }
                        return PopupStep.FINAL_CHOICE
                    }
                }
            ).showInFocusCenter()
        }
    }

    private fun findStatementGenerators(project: Project): List<StatementGenerator> {

        val statementMethodCompletion = project.service<MyBatisProperties>().statementMethodCompletion
        if (statementMethodCompletion.filterWithRegex) {
            if (method.name.matches(Regex(statementMethodCompletion.insertMethodRegex))) {
                return listOf(statementGenerators["insert"]!!)
            } else if (method.name.matches(Regex(statementMethodCompletion.deleteMethodRegex))) {
                return listOf(statementGenerators["delete"]!!)
            } else if (method.name.matches(Regex(statementMethodCompletion.updateMethodRegex))) {
                return listOf(statementGenerators["update"]!!)
            } else if (method.name.matches(Regex(statementMethodCompletion.selectMethodRegex))) {
                return listOf(statementGenerators["select"]!!)
            }
        }


        return statementGenerators.values.toList()
    }

    override fun getName(): String {
        return I18N.message("MyBatis.StatementNotExistsQuickFix.name")
    }


    interface StatementGenerator {
        fun generate(project: Project, method: UMethod)
    }

    abstract class AbstractStatementGenerator<T : Statement> : StatementGenerator {

        final override fun generate(project: Project, method: UMethod) {
            val uClass = method.getContainingUClass() ?: return
            val mappers = project.service<MapperService>().findMappers(uClass.qualifiedName!!)
            if (mappers.isEmpty()) {
                // mapper 不存在，还需要创建mapper
            } else if (mappers.size == 1) {
                // 只有一个mapper，直接添加
                val mapper = mappers.iterator().next()
                val statement = generateStatement(mapper, method)
                statement.getId().stringValue = method.name
                val tag: XmlTag = statement.xmlTag!!
                val offset = tag.textOffset + tag.textLength - tag.name.length - 3
                val editorService: FileEditorManager = FileEditorManager.getInstance(method.project)
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


        protected abstract fun generateStatement(mapper: Mapper, method: UMethod): T

        protected abstract fun getDisplayText(): String

        override fun toString(): String {
            return getDisplayText()
        }
    }

    class InsertStatementGenerator : AbstractStatementGenerator<Insert>() {
        override fun generateStatement(mapper: Mapper, method: UMethod): Insert {
            return mapper.addInsert()
        }

        override fun getDisplayText(): String {
            return "Insert"
        }
    }

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
            return mapper.addUpdate().apply {

            }
        }

        override fun getDisplayText(): String {
            return "Update"
        }
    }

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
        override fun generateStatement(mapper: Mapper, method: UMethod): Delete {
            return mapper.addDelete()
        }

        override fun getDisplayText(): String {
            return "Delete"
        }
    }

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
        override fun generateStatement(mapper: Mapper, method: UMethod): Select {

            val returnType = resolveReturnType(method)

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

                val project = method.project
                val tableSqlFragment = MapperUtils.getTableSqlFragment(project, mapper)
                val columnSqlFragment = MapperUtils.getColumnSqlFragment(project, mapper)

                var addedElement: PsiElement? = null

                Stream.of(
                    "SELECT ",
                    "<include refid=\"${columnSqlFragment.getId().stringValue}\"/>",
                    "FROM ",
                    "<include refid=\"${tableSqlFragment.getId().stringValue}\"/>",
                    "<where></where>"
                )
                    .forEach { content ->
                        val element = XmlUtils.createElement(project, content)
                        if (addedElement == null) {
                            addedElement = xmlTag!!.add(element)
                        } else {
                            addedElement = xmlTag!!.addAfter(element, addedElement)
                        }
                    }


            }
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
}