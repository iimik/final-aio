package org.ifinalframework.plugins.aio.application;

import org.ifinalframework.plugins.aio.application.annotation.ElementApplication
import org.ifinalframework.plugins.aio.spi.annotation.LanguageSpi
import org.springframework.beans.BeansException
import org.springframework.beans.factory.support.AbstractBeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader
import org.springframework.context.annotation.AnnotationConfigRegistry
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.core.annotation.AnnotationUtils
import java.util.*
import kotlin.reflect.KClass


/**
 * ElementApplicationBeanFactoryPostProcessor
 *
 * @author iimik
 * @since 0.0.1
 **/
class ElementApplicationBeanFactoryPostProcessor : BeanDefinitionRegistryPostProcessor {
    @Throws(BeansException::class)
    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        val reader = AnnotatedBeanDefinitionReader(registry)

        for (beanDefinitionName in registry.beanDefinitionNames) {
            val beanDefinition = registry.getBeanDefinition(beanDefinitionName)
            if (beanDefinition is AbstractBeanDefinition) {
                val beanClass = beanDefinition.beanClass
                val elementApplication = AnnotatedElementUtils.getMergedAnnotation(beanClass, ElementApplication::class.java)
                if (elementApplication != null) {
                    val components: Array<KClass<*>> = elementApplication.value
                    for (component in components) {
                        val languageSpi = AnnotationUtils.getAnnotation(component.java, LanguageSpi::class.java)
                        if (Objects.isNull(languageSpi)) {
                            if (registry is AnnotationConfigRegistry) {
                                registry.register(component.java)
                            } else {
                                reader.register(component.java)
                            }
                        } else {
                            val classes: Array<out KClass<*>> = languageSpi.value
                            classes.forEach { reader.register(it.java) }
                        }
                    }
                }
            }
        }
    }
}
