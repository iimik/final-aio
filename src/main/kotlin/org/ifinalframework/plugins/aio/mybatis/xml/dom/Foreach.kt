package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.util.xml.Attribute
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.Required

/**
 * Foreach
 *
 * ```dtd
 * <!ELEMENT foreach (#PCDATA | include | trim | where | set | foreach | choose | if | bind)*>
 * <!ATTLIST foreach
 * collection CDATA #REQUIRED
 * nullable (true|false) #IMPLIED
 * item CDATA #IMPLIED
 * index CDATA #IMPLIED
 * open CDATA #IMPLIED
 * close CDATA #IMPLIED
 * separator CDATA #IMPLIED
 * >
 * ```
 *
 * @author iimik
 */
interface Foreach : SqlFragment {
    @Required
    @Attribute("collection")
    fun getCollection(): GenericAttributeValue<String>

    @Attribute("item")
    fun getItem(): GenericAttributeValue<String>

    @Attribute("open")
    fun getOpen(): GenericAttributeValue<String>

    @Attribute("close")
    fun getClose(): GenericAttributeValue<String>

    @Attribute("separator")
    fun getSeparator(): GenericAttributeValue<String>


}