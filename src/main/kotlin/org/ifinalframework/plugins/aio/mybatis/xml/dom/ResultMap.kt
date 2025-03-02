package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.util.xml.*
import org.ifinalframework.plugins.aio.mybatis.xml.converter.ClassResolvingConverter


/**
 * ResultMap
 *
 * ```xml
 * <resultMap id="resultMap" type="class">
 *
 * </resultMap>
 * ```
 *
 * @author iimik
 * @since 0.0.4
 **/
/**
 */
interface ResultMap : IdDomElement {
    @Required
    @NameValue
    @Attribute("type")
    @Convert(ClassResolvingConverter::class)
    fun getType(): GenericAttributeValue<String>
}