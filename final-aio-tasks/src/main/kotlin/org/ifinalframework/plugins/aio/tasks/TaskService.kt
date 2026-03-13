package org.ifinalframework.plugins.aio.tasks

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.spi.annotation.LanguageSpi
import org.ifinalframework.plugins.aio.tasks.jvm.JvmTaskService
import org.ifinalframework.plugins.aio.tasks.markdown.MarkdownTaskService


/**
 * IssueService
 *
 * @issue 11
 * @author iimik
 * @since 0.0.2
 **/
@LanguageSpi<TaskService>(
    JvmTaskService::class,
    MarkdownTaskService::class
)
interface TaskService {
    fun getTaskDoc(element: PsiElement): TaskDoc?
}