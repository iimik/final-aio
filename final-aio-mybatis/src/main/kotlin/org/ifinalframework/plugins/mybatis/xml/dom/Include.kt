package org.ifinalframework.plugins.mybatis.xml.dom

import com.intellij.util.xml.*
import org.ifinalframework.plugins.mybatis.xml.converter.SqlConverter

/**
 * Include
 * ```xml
 * <include refid="{sql.id}"/>
 * ```
 * @author iimik
 */
interface Include : DomElement {
    @Required
    @NameValue
    @Attribute("refid")
    @Convert(SqlConverter::class)
    fun getRefid(): GenericAttributeValue<Sql>
}