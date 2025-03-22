package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.psi.PsiMethod
import com.intellij.util.xml.*
import org.ifinalframework.plugins.aio.mybatis.xml.converter.ResultMapConverter
import org.ifinalframework.plugins.aio.mybatis.xml.converter.StatementMethodResolvingConverter


/**
 * Statement
 *
 * @author iimik
 * @since 0.0.4
 **/
interface Statement : DomElement {
    @Required
    @NameValue
    @Attribute("id")
    @Convert(StatementMethodResolvingConverter::class)
    fun getId(): GenericAttributeValue<PsiMethod>

    /**
     * @see [ResultMap.getId]
     */
    @NameValue
    @Attribute("resultMap")
    @Convert(ResultMapConverter::class)
    fun getResultMap(): GenericAttributeValue<String>

    @NameValue
    @Attribute("resultType")
    fun getResultType(): GenericAttributeValue<String>

    fun setValue(content: String)
}