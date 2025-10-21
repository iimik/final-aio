package org.ifinalframework.plugins.aio.util

import com.intellij.codeInsight.navigation.activateFileWithPsiElement
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag

/**
 * EditorUtils
 *
 * @author iimik
 */
object EditorUtils {
    fun open(element: PsiElement){
        val project = element.project
        val offset = getElementOffset(element)
        val editorService: FileEditorManager = FileEditorManager.getInstance(project)
        activateFileWithPsiElement(element, true)
//                editorService.openFile(tag.containingFile!!.virtualFile)
        val editor = editorService.selectedTextEditor
        if (null != editor) {
            editor.caretModel.moveToOffset(offset)
            editor.scrollingModel.scrollToCaret(ScrollType.RELATIVE)
        }
    }

    private fun getElementOffset(element: PsiElement): Int {
        when(element){
            is XmlTag -> return element.textOffset + element.name.length
            else -> return element.textOffset
        }
    }


}