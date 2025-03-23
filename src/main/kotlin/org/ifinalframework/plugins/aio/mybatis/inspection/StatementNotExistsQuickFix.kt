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
import com.intellij.psi.PsiJavaCodeReferenceElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.PsiType
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.psi.xml.XmlTag
import org.ifinalframework.plugins.aio.intellij.GenericQuickFix
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.mybatis.xml.dom.*
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getContainingUClass


/**
 * StatementNotExistsQuickFix
 *
 * @issue 43
 * @author iimik
 * @since 0.0.10
 **/
class StatementNotExistsQuickFix(val method: UMethod) : GenericQuickFix() {

    private val statementGenerators = listOf(
        InsertStatementGenerator(),
        UpdateStatementGenerator(),
        DeleteStatementGenerator(),
        SelectStatementGenerator()
    )


    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        method.getContainingUClass() ?: return
        val generators = findStatementGenerators()

        if (generators.size == 1) {
            val generator = generators[0]
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

    private fun findStatementGenerators(): List<StatementGenerator> {
        val generators = statementGenerators.filter { it.supports(method) }
        if (generators.isNotEmpty()) {
            return generators
        }

        return statementGenerators
    }

    override fun getName(): String {
        return I18N.message("MyBatis.StatementNotExistsQuickFix.name")
    }


    interface StatementGenerator {
        fun generate(project: Project, method: UMethod)

        fun supports(method: UMethod): Boolean
    }

    abstract class AbstractStatementGenerator<T : Statement>(val regex: Regex) : StatementGenerator {
        constructor(regex: String) : this(Regex(regex))

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
                statement.setValue(" ")
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

        override fun supports(method: UMethod): Boolean {
            return regex.matches(method.name)
        }

        protected abstract fun generateStatement(mapper: Mapper, method: UMethod): T

        protected abstract fun getDisplayText(): String

        override fun toString(): String {
            return getDisplayText()
        }
    }

    class InsertStatementGenerator : AbstractStatementGenerator<Insert>("^(insert|add|create)+\\w*\$") {
        override fun generateStatement(mapper: Mapper, method: UMethod): Insert {
            return mapper.addInsert()
        }

        override fun getDisplayText(): String {
            return "Insert"
        }
    }

    class UpdateStatementGenerator : AbstractStatementGenerator<Update>("^(update)+\\w*\$") {
        override fun generateStatement(mapper: Mapper, method: UMethod): Update {
            return mapper.addUpdate()
        }

        override fun getDisplayText(): String {
            return "Update"
        }
    }

    class DeleteStatementGenerator : AbstractStatementGenerator<Delete>("^(delete|remove)+\\w*\$") {
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
     */
    class SelectStatementGenerator : AbstractStatementGenerator<Select>("^(select|find|get|query)+\\w*\$") {
        override fun generateStatement(mapper: Mapper, method: UMethod): Select {

            val returnType = resolveReturnType(method)

            return mapper.addSelect().apply {

                if(returnType is PsiPrimitiveType){
                    getResultType().stringValue = returnType.boxedTypeName
                }else if (returnType is PsiClassReferenceType){
                    val className = returnType.reference.qualifiedName

                    val resultMap = mapper.getResultMaps().firstOrNull { it.getType().stringValue == className }
                    if(resultMap != null){
                        getResultMap().stringValue = resultMap.getId().stringValue
                    }else{
                        getResultType().stringValue = className
                    }

                }
            }
        }

        private fun resolveReturnType(method: PsiMethod): PsiType?{
            val psiType = method.returnType ?: return null

            if(psiType is PsiPrimitiveType){
                return psiType
            }

            if(psiType is PsiClassReferenceType){
                val reference = psiType.reference
                // 如果是参数类型且只有一个参数类型，如List<?>，取第一个元素，
                if(reference is PsiJavaCodeReferenceElement && reference.typeParameters.size == 1){
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