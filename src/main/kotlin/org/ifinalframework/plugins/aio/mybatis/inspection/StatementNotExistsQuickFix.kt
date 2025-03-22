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
 * @author iimik
 * @since 0.0.4
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
                val statement = generateStatement(mapper)
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

        protected abstract fun generateStatement(mapper: Mapper): T

        protected abstract fun getDisplayText(): String

        override fun toString(): String {
            return getDisplayText()
        }
    }

    class InsertStatementGenerator : AbstractStatementGenerator<Insert>("^(insert|add|create)+\\w*\$") {
        override fun generateStatement(mapper: Mapper): Insert {
            return mapper.addInsert()
        }

        override fun getDisplayText(): String {
            return "Insert"
        }
    }

    class UpdateStatementGenerator : AbstractStatementGenerator<Update>("^(update)+\\w*\$") {
        override fun generateStatement(mapper: Mapper): Update {
            return mapper.addUpdate()
        }

        override fun getDisplayText(): String {
            return "Update"
        }
    }

    class DeleteStatementGenerator : AbstractStatementGenerator<Delete>("^(delete|remove)+\\w*\$") {
        override fun generateStatement(mapper: Mapper): Delete {
            return mapper.addDelete()
        }

        override fun getDisplayText(): String {
            return "Delete"
        }
    }

    class SelectStatementGenerator : AbstractStatementGenerator<Select>("^(select|find|get|query)+\\w*\$") {
        override fun generateStatement(mapper: Mapper): Select {
            return mapper.addSelect()
        }

        override fun getDisplayText(): String {
            return "Select"
        }
    }
}