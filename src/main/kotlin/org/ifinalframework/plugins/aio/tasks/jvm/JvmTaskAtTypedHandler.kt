package org.ifinalframework.plugins.aio.tasks.jvm

import com.intellij.codeInsight.completion.CodeCompletionHandlerBase
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaTokenType
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.lexer.KtTokens

/**
 * JVM 单行注释 Task 补全触发器
 * JVM 语言中默认单行注释`// @`不会触发提示，目前支持以下语言
 * - Java
 * - Kotlin
 * @since 0.0.24
 */
class JvmTaskAtTypedHandler : TypedHandlerDelegate() {

    override fun charTyped(
        c: Char,
        project: Project,
        editor: Editor,
        file: PsiFile
    ): Result {

        if (c != '@') {
            return Result.CONTINUE
        }

        val offset = editor.caretModel.offset - 1
        if (offset < 0) return Result.CONTINUE
        val element = file.findElementAt(offset)
        val language = file.language
        if(JavaLanguage.INSTANCE == language){
            if (element?.prevSibling?.elementType == JavaTokenType.END_OF_LINE_COMMENT) {
                ApplicationManager.getApplication().invokeLater {
                    CodeCompletionHandlerBase.createHandler(CompletionType.BASIC).invokeCompletion(project, editor)
                }
            }
        }else if(KotlinLanguage.INSTANCE == language){
            if (element.elementType == KtTokens.EOL_COMMENT) {
                ApplicationManager.getApplication().invokeLater {
                    CodeCompletionHandlerBase.createHandler(CompletionType.BASIC).invokeCompletion(project, editor)
                }
            }
        }

        return Result.CONTINUE
    }
}