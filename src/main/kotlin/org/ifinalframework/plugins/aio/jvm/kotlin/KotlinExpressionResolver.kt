package org.ifinalframework.plugins.aio.jvm.kotlin;

import org.ifinalframework.plugins.aio.jvm.ExpressionResolver
import org.jetbrains.kotlin.psi.KtCollectionLiteralExpression
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
            is KtCollectionLiteralExpression -> expression.innerExpressions.map { resolve(it) }.toList()
            else -> expression
        }
    }

}