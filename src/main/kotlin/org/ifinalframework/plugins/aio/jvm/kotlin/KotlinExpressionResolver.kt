package org.ifinalframework.plugins.aio.jvm.kotlin;

import org.ifinalframework.plugins.aio.jvm.ExpressionResolver
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtCollectionLiteralExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression


/**
 * KotlinExpressionResolver
 *
 * @author iimik
 * @since 0.0.2
 **/
class KotlinExpressionResolver : ExpressionResolver<KtExpression> {
    override fun resolve(expression: KtExpression): Any? {
        return when (expression) {
            is KtStringTemplateExpression -> expression.text.trim('"')
            is KtCollectionLiteralExpression -> expression.innerExpressions.map { resolve(it) }.toList()
            else -> expression
        }
    }

}