package org.ifinalframework.plugins.aio.issue

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.spi.annotation.LanguageSpi


/**
 * IssueService
 *
 * @issue 11
 * @author iimik
 * @since 0.0.2
 **/
@LanguageSpi<IssueService>(
    JvmIssueService::class,
    MarkdownIssueService::class
)
interface IssueService {
    fun getIssue(element: PsiElement): Issue?
}