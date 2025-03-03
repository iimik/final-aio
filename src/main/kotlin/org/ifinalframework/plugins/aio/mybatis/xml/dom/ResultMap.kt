package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.util.xml.*
import org.ifinalframework.plugins.aio.mybatis.xml.converter.ResultMapConverter
import org.ifinalframework.plugins.aio.mybatis.xml.converter.ResultMapIdReferenceConverter


/**
 * ResultMap
 *
 * ```xml
 * <resultMap id="resultMap" type="class" extends="resultMapId">
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

    /**
     * @see [Statement.getResultMap]
     */
    @Required
    @NameValue
    @Attribute("id")
    @Referencing(ResultMapIdReferenceConverter::class)
    override fun getId(): GenericAttributeValue<String>

    @Required
    @NameValue
    @Attribute("type")
    fun getType(): GenericAttributeValue<String>

    @NameValue
    @Attribute("extends")
    @Convert(ResultMapConverter::class)
    fun getExtends(): GenericAttributeValue<String>

}