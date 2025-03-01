package org.ifinalframework.plugins.aio.api.service;

import com.intellij.openapi.components.service
import com.intellij.openapi.module.Module
import org.ifinalframework.plugins.aio.api.ApiConfigs
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.common.util.getBasePath
import org.ifinalframework.plugins.aio.service.EnvironmentService
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownFile
import org.jetbrains.kotlin.idea.core.util.toPsiFile
import java.io.File


/**
 * DefaultMarkdownService
 *
 * @author iimik
 * @since 0.0.6
 **/
class DefaultMarkdownService : MarkdownService {
    override fun findMarkdownFile(module: Module, marker: ApiMarker): MarkdownFile? {
        val environmentService = module.project.service<EnvironmentService>()
        val markdownBasePath = environmentService.getProperty(module, ApiConfigs.MarkdownBasePath)!!
        val markdownFilePath = "$markdownBasePath/${marker.category.replace("-", "/")}/${marker.name}.md"
        val path = "${module.getBasePath()}/${markdownFilePath}"
        val file = File(path)
        if (file.exists() && file.isFile) {
            val psiFile = file.toPsiFile(module.project)
            if (psiFile is MarkdownFile) {
                return psiFile
            }
        }

        return null;
    }
}