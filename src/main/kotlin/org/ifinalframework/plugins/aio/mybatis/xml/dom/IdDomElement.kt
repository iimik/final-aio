package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.util.xml.*


/**
 * IdDomElement
 *
 * @author iimik
 * @since 0.0.4
 * @see [ResultMap]
 **/
interface IdDomElement : DomElement {
    @Required
    @NameValue
    @Attribute("id")
    fun getId(): GenericAttributeValue<String>

    fun setValue(content: String)
}