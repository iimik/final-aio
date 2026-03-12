package org.ifinalframework.plugins.aio.jvm

import com.intellij.psi.PsiJvmModifiersOwner
import org.ifinalframework.plugins.aio.core.annotation.AnnotationAttributes
import org.ifinalframework.plugins.aio.jvm.java.JavaExpressionResolver

/**
 * DefaultAnnotationService
 *
 * @author iimik
 */
class DefaultAnnotationService : AnnotationService {

    private val expressionResolver = JavaExpressionResolver()

    override fun findAnnotationAttributes(element: PsiJvmModifiersOwner, fqn: String): AnnotationAttributes? {
        val annotation = element.getAnnotation(fqn)?:return null
        val map = mutableMapOf<String, Any?>()
        annotation.parameterList.attributes.forEach { attribute ->
            val name = attribute.name ?: "value"
            val attributeValue = attribute.value
            val value = attributeValue?.let { expressionResolver.resolve(it) }
            map[name] = value
        }
        return AnnotationAttributes.fromMap(map)
    }
}