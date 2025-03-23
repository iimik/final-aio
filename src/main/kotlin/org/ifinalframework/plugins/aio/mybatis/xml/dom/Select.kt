package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.psi.PsiClass
import com.intellij.util.xml.Attribute
import com.intellij.util.xml.Convert
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import org.ifinalframework.plugins.aio.mybatis.xml.converter.ResultMapConverter


/**
 * Select
 *
 * @author iimik
 * @since 0.0.4
 **/
interface Select : Statement {

    @NameValue
    @Attribute("resultType")
    fun getResultType(): GenericAttributeValue<PsiClass>

    /**
     * @see [ResultMap.getId]
     */
    @NameValue
    @Attribute("resultMap")
    @Convert(ResultMapConverter::class)
    fun getResultMap(): GenericAttributeValue<String>

}