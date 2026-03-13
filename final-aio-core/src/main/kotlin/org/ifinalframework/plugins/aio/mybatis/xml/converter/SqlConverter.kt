package org.ifinalframework.plugins.aio.mybatis.xml.converter

import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.xml.ConvertContext
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils

/**
 * SqlConverter
 * ```xml
 * <include refid={sqlid}/>
 * ```
 * @author iimik
 */
class SqlConverter : AbsConverter<XmlAttributeValue>() {

    override fun fromString(s: String?, context: ConvertContext): XmlAttributeValue? {
        if (s == null) return null

        val mapper = MyBatisUtils.getMapper(context.invocationElement)
        return mapper.getSqls()
            .map { it.getId().xmlAttributeValue }
            .firstOrNull { it?.value == s.trim() }
    }

}