package org.ifinalframework.plugins.aio.jvm.java;

import com.intellij.psi.*
import org.ifinalframework.plugins.aio.jvm.ExpressionResolver


/**
 * JavaExpressionResolver
 * @issue 20
 * @author iimik
 * @since 0.0.2
 **/
class JavaExpressionResolver : ExpressionResolver<PsiElement> {
    override fun resolve(expression: PsiElement): Any? {
        return when (expression) {
            is PsiLiteralExpression -> expression.value
            is PsiReferenceExpression -> doResolveReferenceExpression(expression)
            is PsiArrayInitializerMemberValue -> expression.initializers.map { resolve(it) }.toList()
            else -> expression
        }
    }

    private fun doResolveReferenceExpression(expression: PsiReferenceExpression): Any? {
        return expression.text.substringAfterLast(".")
    }

    private fun doResolve(expression: PsiExpression): Any? {
        return when (expression) {
            is PsiLiteralExpression -> expression.value
            else -> expression
        }
    }
}