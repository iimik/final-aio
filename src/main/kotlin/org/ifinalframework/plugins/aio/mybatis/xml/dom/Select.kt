package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.psi.PsiClass
import com.intellij.util.xml.Attribute
import com.intellij.util.xml.Convert
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.NameValue
import org.ifinalframework.plugins.aio.mybatis.xml.converter.ResultMapConverter


/**
 * ```xml
 * <select id="">
 * ```
 *
 * @author iimik
 * @since 0.0.4
 **/
interface Select : Statement {

    /**
     * 查询返回的结果类型
     */
    @NameValue(unique = false)
    @Attribute("resultType")
    fun getResultType(): GenericAttributeValue<PsiClass>

    /**
     * 查询返回的结果集
     * @see [ResultMap]
     */
    @NameValue(unique = false)
    @Attribute("resultMap")
    @Convert(ResultMapConverter::class)
    fun getResultMap(): GenericAttributeValue<ResultMap>

}