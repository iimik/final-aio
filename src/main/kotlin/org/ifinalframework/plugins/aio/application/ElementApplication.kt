package org.ifinalframework.plugins.aio.application;

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.application.aop.AopConfig
import org.springframework.aop.framework.AopConfigException
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.util.ClassUtils
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.reflect.KClass


/**
 * ElementApplication
 *
 * @author iimik
 * @since 0.0.1
 **/
internal class ElementApplication(
    private val primarySource: KClass<*>,
    private val element: PsiElement?,
) {

    private val logger = logger<ElementApplication>()

    fun run(vararg args: String?): ApplicationContext {
        val lang = element?.language?.id?.lowercase(Locale.getDefault())
        val module = if (element != null) ModuleUtil.findModuleForPsiElement(element) else null
        val project = element?.project
        val classLoader = AopConfigException::class.java.classLoader
        val onClassCondition = ClassUtils.forName("org.springframework.boot.autoconfigure.condition.OnClassCondition", classLoader)
        val context = AnnotationConfigApplicationContext()
        try {
            //fix: 启动报 ClassNotFoundException AopConfigException

            context.classLoader = classLoader
            Thread.currentThread().contextClassLoader = classLoader
            context.beanFactory.beanClassLoader = classLoader
            if (element != null) {
                System.setProperty("final.language", lang)
                System.setProperty("final.jvm", (lang == "java" || lang == "kotlin").toString())
                val environment = ModuleEnvironment()
                environment.load(classLoader, module!!)
                context.environment = environment
                // bean factory post processor
                context.addBeanFactoryPostProcessor(ElementApplicationBeanFactoryPostProcessor())
                // element components
                context.registerBean("element", PsiElement::class.java, Supplier { element })
                context.registerBean(
                    "module", Module::class.java, Supplier { module })
                context.registerBean(
                    "project", Project::class.java, Supplier { project })
            }
            // primarySources
            context.register(primarySource.java)
            // aop
            context.register(AopConfig::class.java)
//            val hasFeign = AnnotatedElementUtils.isAnnotated(primarySource.java, EnableFeignClients::class.java)
//            if (hasFeign) {
//                // feign
//                context.register(JacksonAutoConfiguration::class.java);
//                context.register(FeignClientsConfiguration::class.java);
//                context.register(HttpMessageConvertersAutoConfiguration::class.java);
//                context.register(FeignAutoConfiguration::class.java);
//            }
            // refresh
            context.refresh()
//            if (hasFeign) {
//                val objectMapper = context.getBean(ObjectMapper::class.java)
//                objectMapper.registerModule(KotlinModule.Builder().build())
//            }

            // handle
            val elementHandlers: List<ElementHandler> =
                context.getBeanProvider<ElementHandler>(ElementHandler::class.java).stream().toList()
            if (elementHandlers.isEmpty()) {
                logger.warn("not found handlers for application: $primarySource")
                return context
            }
            if (element != null) {
                elementHandlers.forEach(Consumer<ElementHandler> { handler: ElementHandler ->
                    try {
                        handler.handle(element)
                    } catch (e: Exception) {
                        logger.error(e.message, e)
                    }
                })
            }
        } catch (e: Exception) {
            logger.error("处理异常：", e)
        }

        return context
    }

    companion object {
        fun <T : Any> run(primaryClass: KClass<T>, element: PsiElement? = null): ApplicationContext {
            return ElementApplication(primaryClass, element).run()
        }
    }
}