package org.ifinalframework.plugins.aio.api.inspection

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.components.service
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.api.service.MarkdownService
import org.ifinalframework.plugins.aio.intellij.GenericQuickFix
import org.ifinalframework.plugins.aio.resource.I18N


/**
 * 快速生成Api Markdown
 *
 * @author iimik
 * @since 0.0.6
 **/
class MarkdownNotExistsQuickFix(
    private val module: Module,
    private val apiMarker: ApiMarker
) : LocalQuickFix {

    override fun getFamilyName(): String {
        return I18N.message("Api.MarkdownNotExistsQuickFix.familyName")
    }
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        service<MarkdownService>().createMarkdownFile(module, apiMarker)
    }
}