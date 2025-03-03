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
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.util.*
import java.util.stream.Collectors


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
                    val markdown = markdownFile.virtualFile.readText()
                    val path = markdownFile.virtualFile.path.substringBeforeLast("/")
                    val newMarkdown = markdown!!.split("\n").stream()
                        .map {
                            if (it.trim().startsWith("![")) {
                                // 图片 ![]()
                                val start = it.indexOf("(");
                                val end = it.indexOf(")");
                                val imageFile = it.substring(start + 1, end)

                                val bytes = BufferedInputStream(FileInputStream("$path/$imageFile")).readBytes()
                                val imageBase64 = Base64.getEncoder().encodeToString(bytes)
                                val newImageLine = it.substring(0, start) + "(data:image/png;base64," + imageBase64 + ")"
                                return@map newImageLine

                            } else {
                                return@map it;
                            }
                        }
                        .collect(Collectors.joining("\n"))
                    return markdownHtmlGenerator.render(newMarkdown)
                }
            }
        }
        return null
    }

}