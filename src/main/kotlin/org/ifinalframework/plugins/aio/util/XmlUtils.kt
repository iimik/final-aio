package org.ifinalframework.plugins.aio.util

import com.intellij.lang.ASTFactory
import com.intellij.openapi.project.Project
import com.intellij.psi.XmlElementFactory
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlElementType
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlText

/**
 * XmlUtils
 *
 * @author iimik
 */
object XmlUtils {

    /**
     * 创建xml元素，不使用CDATA标签包裹文本
     * @see [XmlElementFactory.createDisplayText]
     * @see [XmlElementFactory.createTagFromText]
     */
    fun createElement(project: Project, content: String): XmlElement {
        val factory = XmlElementFactory.getInstance(project)
        val element = if (content.startsWith("<")) {
            factory.createTagFromText(content)
        } else {
            val tagFromText: XmlTag = factory.createTagFromText("<a>\n$content\n</a>")
            val textElements = tagFromText.getValue().getTextElements()
            if (textElements.size == 0) {
                ASTFactory.composite(XmlElementType.XML_TEXT) as XmlText
            } else {
                textElements[0]

            }
        }
        return element
    }
}