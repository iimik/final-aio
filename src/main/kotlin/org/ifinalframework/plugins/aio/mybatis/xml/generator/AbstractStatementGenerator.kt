package org.ifinalframework.plugins.aio.mybatis.xml.generator

import com.intellij.codeInsight.navigation.activateFileWithPsiElement
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Statement
import org.ifinalframework.plugins.aio.util.XmlUtils
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getContainingUClass

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

    protected abstract fun getDisplayText(): String

    override fun toString(): String {
        return getDisplayText()
    }
}
