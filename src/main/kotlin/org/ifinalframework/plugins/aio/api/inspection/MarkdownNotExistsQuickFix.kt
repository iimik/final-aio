package org.ifinalframework.plugins.aio.api.inspection

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.api.service.MarkdownService
import org.ifinalframework.plugins.aio.intellij.GenericQuickFix
import org.jetbrains.kotlin.idea.base.util.module


/**
 * 快速生成Api Markdown
 *
 * @author iimik
 * @since 0.0.6
 **/
class MarkdownNotExistsQuickFix(
    private val element: PsiElement,
    private val apiMarker: ApiMarker
) : GenericQuickFix() {
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val module = element.module ?: return
        service<MarkdownService>().createMarkdownFile(module, apiMarker)
    }
}