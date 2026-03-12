package org.ifinalframework.plugins.aio.application;

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtil
import com.intellij.psi.PsiElement
import org.apache.commons.lang3.StringUtils
import org.ifinalframework.plugins.aio.common.util.getBasePath
import java.util.*
import kotlin.collections.ArrayList


/**
 * DefaultConfigService
 *
 * @author iimik
 * @since 0.0.1
 **/
class DefaultConfigService : ConfigService {

    override fun getConfigPaths(module: Module): List<String> {

        val modulePath: String = module.getBasePath()
        val projectPath: String = module.project.basePath!!

        if(modulePath == projectPath) {
            return Collections.singletonList(projectPath)
        }

        var configPath = modulePath

        val configPaths: MutableList<String> = ArrayList()

        do {
            configPaths.add(configPath)
            configPath = StringUtils.substringBeforeLast(configPath, "/")
        } while (configPath != projectPath)

        configPaths.add(projectPath)

        return configPaths
    }
}