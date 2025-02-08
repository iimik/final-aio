package org.ifinalframework.plugins.aio.jvm.java;

import com.intellij.psi.PsiArrayInitializerMemberValue
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiLiteralExpression
import org.ifinalframework.plugins.aio.jvm.ExpressionResolver


/**
 * JavaExpressionResolver
 *
 * @author iimik
 * @since 0.0.2
 **/
class JavaExpressionResolver : ExpressionResolver<PsiElement> {
    override fun resolve(expression: PsiElement): Any? {
        return when (expression) {
            is PsiLiteralExpression -> expression.value
            is PsiArrayInitializerMemberValue -> expression.initializers.map { resolve(it) }.toList()
            else -> expression
        }
    }

    private fun doResolve(expression: PsiExpression): Any? {
        return when (expression) {
            is PsiLiteralExpression -> expression.value
            else -> expression
        }
    }
}