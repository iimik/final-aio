package org.ifinalframework.plugins.aio.jvm

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.application.annotation.ReadAction


/**
 * PsiExpressionResolver
 *
 * @issue 19
 * @author iimik
 * @since 0.0.2
 **/
interface ExpressionResolver<T : PsiElement> {
    @ReadAction
    fun resolve(expr: T): Any?
}