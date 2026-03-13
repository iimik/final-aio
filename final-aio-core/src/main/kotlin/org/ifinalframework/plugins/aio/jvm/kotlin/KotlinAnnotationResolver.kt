package org.ifinalframework.plugins.aio.jvm.kotlin;

import org.ifinalframework.plugins.aio.application.condition.ConditionOnKotlin
import org.ifinalframework.plugins.aio.core.annotation.AnnotationAttributes
import org.ifinalframework.plugins.aio.jvm.AnnotationResolver
import org.ifinalframework.plugins.aio.jvm.ExpressionResolver
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtExpression
import org.springframework.stereotype.Component


/**
 * KotlinAnnotationResolver
 *
 * @author iimik
 * @since 0.0.2
 **/
@Component
@ConditionOnKotlin
class KotlinAnnotationResolver(
    private val expressionResolver: ExpressionResolver<KtExpression> = KotlinExpressionResolver()
) : AnnotationResolver<KtAnnotationEntry> {
    override fun resolve(annotation: KtAnnotationEntry): AnnotationAttributes {
        val result = mutableMapOf<String, Any?>()

        annotation.valueArguments.forEach { valueArgument ->
            val name = valueArgument.getArgumentName()?.asName?.asString() ?: "value"
            val value = valueArgument.getArgumentExpression()?.let { expressionResolver.resolve(it) }
            result[name] = value
        }

        return AnnotationAttributes.fromMap(result)
    }
}