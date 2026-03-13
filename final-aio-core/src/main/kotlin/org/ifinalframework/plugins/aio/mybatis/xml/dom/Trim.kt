package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.util.xml.Attribute
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue

/**
 * Trim
 * ```xml
 * <trim prefix="(" suffix=")" suffixOverrides=",">
 *
 * </trim>
 * ```
 * @author iimik
 */
interface Trim : SqlFragment {
    @NameValue(unique = false)
    @Attribute("prefix")
    fun getPrefix(): GenericAttributeValue<String>

    @NameValue(unique = false)
    @Attribute("suffix")
    fun getSuffix(): GenericAttributeValue<String>

    @NameValue(unique = false)
    @Attribute("prefixOverrides")
    fun getPrefixOverrides(): GenericAttributeValue<String>

    @Attribute("suffixOverrides")
    fun getSuffixOverrides(): GenericAttributeValue<String>

}