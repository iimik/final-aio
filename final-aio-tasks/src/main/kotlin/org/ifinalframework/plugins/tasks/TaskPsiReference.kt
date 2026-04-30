package org.ifinalframework.plugins.tasks

import com.intellij.openapi.components.service
import com.intellij.openapi.paths.WebReference
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.tasks.TaskDoc
import org.ifinalframework.plugins.aio.tasks.TaskDocProcessor


/**
 * TaskPsiReference
 *
 * @author iimik
 * @since 0.0.25
 **/
class TaskPsiReference(
    element: PsiElement,
    textRange: TextRange,
    val taskDoc: TaskDoc
) : WebReference(element, textRange) {
    override fun getValue(): @NlsSafe String {
        return service<TaskDocProcessor>().buildUrl(element.project, taskDoc) ?: ""
    }
}