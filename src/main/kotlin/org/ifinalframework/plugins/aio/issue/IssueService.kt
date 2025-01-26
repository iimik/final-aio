package org.ifinalframework.plugins.aio.issue

import com.intellij.psi.PsiElement


/**
 * IssueService
 *
 * @issue 11
 * @author iimik
 * @since 0.0.2
 **/
interface IssueService {

    fun getIssue(element: PsiElement): Issue?

    fun parseDocTagIssue(name: String, value: String): Issue?

    fun parseLineIssue(comment: String): Issue?
}