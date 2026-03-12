package org.ifinalframework.plugins.aio.mybatis.xml.converter

import com.intellij.psi.PsiElement
import com.intellij.util.xml.ConvertContext
import com.intellij.util.xml.DomUtil
import org.ifinalframework.plugins.aio.mybatis.xml.dom.ResultMap

/**
 *
 * ```xml
 * @author iimik
 * @since 0.0.7
 * @see [org.ifinalframework.plugins.aio.mybatis.xml.dom.ResultMap.Property]
 */
class ResultMapPropertyConverter : AbsConverter<PsiElement>() {
    override fun fromString(property: String?, context: ConvertContext): PsiElement? {
        if (property == null) return null
        val resultMap = DomUtil.getParentOfType(context.invocationElement, ResultMap::class.java, true) ?: return null
        val clazz = resultMap.getType().value ?: return null
        val field = clazz.findFieldByName(property, true) ?: return null

        return field
    }
}