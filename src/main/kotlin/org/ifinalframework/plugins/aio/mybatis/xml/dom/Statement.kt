package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.util.xml.*
import org.ifinalframework.plugins.aio.mybatis.xml.converter.StatementMethodResolvingConverter


/**
 * Statement
 *
 * @author iimik
 * @since 0.0.4
 **/
interface Statement : IdDomElement {
    @Required
    @NameValue
    @Attribute("id")
    @Convert(StatementMethodResolvingConverter::class)
    override fun getId(): GenericAttributeValue<String>
}