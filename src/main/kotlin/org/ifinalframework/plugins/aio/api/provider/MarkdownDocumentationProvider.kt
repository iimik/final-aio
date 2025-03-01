package org.ifinalframework.plugins.aio.api.provider

import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.readText
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import org.ifinalframework.plugins.aio.api.service.MarkdownService
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.markdown.MarkdownHtmlGenerator
import org.jetbrains.kotlin.idea.base.util.module


/**
 * 自定义Markdown文档提供者
 *
 * @issue 30
 * @author iimik
 * @since 0.0.6
 **/
class MarkdownDocumentationProvider : DocumentationProvider {

    private val markdownHtmlGenerator = MarkdownHtmlGenerator()

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        if (element != null && element is PsiMethod) {
            val apiMarker = service<ApiMethodService>().getApiMarker(element)
            if (apiMarker != null) {
                val markdownFile = service<MarkdownService>().findMarkdownFile(element.module!!, apiMarker)
                if (markdownFile != null) {
                    return markdownHtmlGenerator.render(markdownFile.virtualFile.readText())
                }
            }
        }
        return null
    }

}