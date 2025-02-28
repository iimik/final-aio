package org.ifinalframework.plugins.aio.service

import com.intellij.ide.impl.ProjectUtil
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.module.Module
import org.ifinalframework.plugins.aio.application.ModuleEnvironment
import org.springframework.core.env.Environment
import kotlin.reflect.KClass


/**
 * SpringModuleEnvironmentService
 *
 * @author iimik
 * @since 0.0.5
 **/
class SpringModuleEnvironmentService : EnvironmentService {

    private val logger = logger<SpringModuleEnvironmentService>()
    private val cache = mutableMapOf<Module, Environment>()

    override fun getProperty(module: Module, key: String): String? {
        return getEnvironment(module).getProperty(key)
    }

    override fun <T : Any> getProperty(module: Module, key: String, clazz: KClass<T>, value: T): T {
        logger.info("getProperty: ${module.name}: $key -> $value")
        return getEnvironment(module).getProperty(key, clazz.java, value) as T
    }

    override fun reset() {
        cache.clear()
        val activeProject = ProjectUtil.getActiveProject()!!
        service<NotificationService>().notify(
            NotificationDisplayType.STICKY_BALLOON,
            "项目${activeProject.name}清空配置缓存",
            NotificationType.INFORMATION
        )
    }

    private fun getEnvironment(module: Module): Environment {
        return cache.computeIfAbsent(module) { createEnvironment(it) }
    }

    private fun createEnvironment(module: Module): ModuleEnvironment {
        val env = ModuleEnvironment()
        env.load(javaClass.classLoader, module)
        return env
    }

}