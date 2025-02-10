package org.ifinalframework.plugins.aio.jvm.kotlin;

import org.ifinalframework.plugins.aio.jvm.ExpressionResolver
import org.jetbrains.kotlin.psi.KtCollectionLiteralExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression
import org.jetbrains.kotlin.psi.psiUtil.plainContent


/**
 * KotlinExpressionResolver
 * @issue 21
 * @author iimik
 * @since 0.0.2
 **/
class KotlinExpressionResolver : ExpressionResolver<KtExpression> {
    override fun resolve(expression: KtExpression): Any? {
        return when (expression) {
            is KtStringTemplateExpression -> expression.plainContent
            is KtDotQualifiedExpression -> doResolveDotQualifiedExpression(expression)
            is KtCollectionLiteralExpression -> expression.innerExpressions.map { resolve(it) }.toList()
            else -> expression
        }
    }

    private fun doResolveDotQualifiedExpression(expression: KtDotQualifiedExpression): Any? {
        return expression.text.substringAfterLast(".")
    }

}