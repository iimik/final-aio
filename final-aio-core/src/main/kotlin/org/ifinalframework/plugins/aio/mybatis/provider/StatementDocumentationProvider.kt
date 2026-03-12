package org.ifinalframework.plugins.aio.mybatis.provider

import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.util.xml.DomUtil
import org.ifinalframework.plugins.aio.markdown.MarkdownHtmlGenerator
import org.ifinalframework.plugins.aio.mybatis.service.JvmMapperLineMarkerService
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Statement

/**
 * MyBatis Statement Document
 *
 * @author iimik
 * @issue 79
 */
class StatementDocumentationProvider : DocumentationProvider {
    private val markdownHtmlGenerator = MarkdownHtmlGenerator()
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {

        if(originalElement == null) return null
        val marker = service<JvmMapperLineMarkerService>().apply(originalElement)
        if(marker == null || marker.targets == null){
            return null
        }

        val statement = marker.targets.first()

        val text = (DomUtil.getDomElement(statement) as Statement).xmlElement?.text
        return markdownHtmlGenerator.render("```xml\n$text\n```")

    }
}