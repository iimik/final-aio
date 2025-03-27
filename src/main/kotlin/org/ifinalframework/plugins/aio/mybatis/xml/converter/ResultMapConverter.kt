package org.ifinalframework.plugins.aio.mybatis.xml.converter

import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.xml.ConvertContext
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils


/**
 * 解析xml中的属性，指向`<resultMap/>`标签。
 *
 * * resultMap.extends
 * ```xml
 * <resultMap extends="">
 * </resultMap>
 * ```
 * * statement.resultMap
 * ```xml
 * <select resultMap="">
 * </select>
 * ```
 *
 * @see [org.ifinalframework.plugins.aio.mybatis.xml.dom.Select.getResultMap]
 * @see [org.ifinalframework.plugins.aio.mybatis.xml.dom.ResultMap.getExtends]
 * @author iimik
 * @since 0.0.6
 * @see [ResultMapIdReferenceConverter]
 **/
class ResultMapConverter : AbsConverter<XmlAttributeValue>() {

    override fun fromString(s: String?, context: ConvertContext): XmlAttributeValue? {
        if (s == null) return null

        val mapper = MyBatisUtils.getMapper(context.invocationElement)
        return mapper.getResultMaps()
            .map { it.getId().xmlAttributeValue }
            .firstOrNull { it?.value == s.trim() }
    }

}