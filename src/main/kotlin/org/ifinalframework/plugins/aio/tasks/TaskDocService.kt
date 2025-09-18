package org.ifinalframework.plugins.aio.tasks

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.spi.annotation.LanguageSpi


/**
 * IssueService
 *
 * @issue 11
 * @author iimik
 * @since 0.0.2
 **/
@LanguageSpi<TaskDocService>(
    JvmTaskDocService::class,
    MarkdownTaskDocService::class
)
interface TaskDocService {
    fun getTaskDoc(element: PsiElement): TaskDoc?
}