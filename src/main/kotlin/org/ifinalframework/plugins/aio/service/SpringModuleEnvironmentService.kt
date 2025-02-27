package org.ifinalframework.plugins.aio.service

import com.intellij.openapi.module.Module
import org.ifinalframework.plugins.aio.application.ModuleEnvironment


/**
 * SpringModuleEnvironmentService
 *
 * @author iimik
 * @since 0.0.5
 **/
class SpringModuleEnvironmentService(
    private val module: Module
) : EnvironmentService {

    private val environment: ModuleEnvironment by lazy {
        val env = ModuleEnvironment();
        env.load(module.javaClass.classLoader.parent, module);
        env
    }

    override fun getProperty(key: String): String? {
        return environment.getProperty(key)
    }
}