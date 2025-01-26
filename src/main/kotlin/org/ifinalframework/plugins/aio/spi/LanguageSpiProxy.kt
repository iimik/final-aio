package org.ifinalframework.plugins.aio.spi;

import com.intellij.psi.PsiElement
import com.intellij.util.containers.stream
import org.ifinalframework.plugins.aio.application.condition.ConditionOnLanguage
import org.springframework.core.annotation.AnnotatedElementUtils
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors


/**
 * LanguageProxy
 *
 * @author iimik
 * @since 0.0.1
 **/
class LanguageSpiProxy : InvocationHandler {

    private val services: Map<String, Any>

    constructor(services: List<Any>) {
        this.services = services.stream()
            .collect(Collectors.toMap({ obj: Any -> findLanguage(obj.javaClass) }, Function.identity()))
    }

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        val element = args.stream().filter { it is PsiElement }.findFirst().orElse(null) as PsiElement
        if (Objects.isNull(element)) {
            throw IllegalArgumentException("element must not be null")
        }

        val language = element.language.id.lowercase()

        val service = services[language] ?: services["uast"] ?: return null;
        return method?.invoke(service, *args!!)

    }

    private fun findLanguage(clazz: Class<*>): String? {
        return AnnotatedElementUtils.getMergedAnnotation(clazz, ConditionOnLanguage::class.java).value.lowercase()
    }
}