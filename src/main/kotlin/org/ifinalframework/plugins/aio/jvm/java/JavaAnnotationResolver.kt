package org.ifinalframework.plugins.aio.jvm.java;

import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.application.condition.ConditionOnJava
import org.ifinalframework.plugins.aio.jvm.AnnotationResolver
import org.ifinalframework.plugins.aio.jvm.ExpressionResolver
import org.springframework.stereotype.Component


/**
 * JavaAnnotationResolver
 *
 * @author iimik
 * @since 0.0.2
 **/
@Component
@ConditionOnJava
class JavaAnnotationResolver(
    private val expressionResolver: ExpressionResolver<PsiElement> = JavaExpressionResolver()
) : AnnotationResolver<PsiAnnotation> {
    override fun resolve(annotation: PsiAnnotation): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        annotation.parameterList.attributes.forEach { attribute ->
            val name = attribute.name ?: "value"
            val attributeValue = attribute.value
            val value = attributeValue?.let { expressionResolver.resolve(it) }
            map[name] = value
        }
        return map
    }
}