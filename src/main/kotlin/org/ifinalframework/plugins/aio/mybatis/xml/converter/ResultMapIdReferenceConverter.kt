package org.ifinalframework.plugins.aio.mybatis.xml.converter

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.util.xml.ConvertContext
import com.intellij.util.xml.CustomReferenceConverter
import com.intellij.util.xml.GenericAttributeValue
import com.intellij.util.xml.GenericDomValue
import com.intellij.util.xml.impl.GenericDomValueReference
import org.ifinalframework.plugins.aio.mybatis.MyBatisUtils


/**
 * ResultMapIdReferenceConverter
 *
 * @author iimik
 * @since 0.0.6
 **/
class ResultMapIdReferenceConverter : CustomReferenceConverter<GenericAttributeValue<String>> {
    override fun createReferences(
        value: GenericDomValue<GenericAttributeValue<String>>?,
        element: PsiElement?,
        context: ConvertContext?
    ): Array<PsiReference> {

        if (value == null || context == null) return emptyArray()
        val resultMapId = value.rawText ?: return emptyArray()

        val mapper = MyBatisUtils.getMapper(context.invocationElement)

        val resultMaps = mapper.getSelects().filter { resultMapId == it.getResultMap().rawText }.map { it.getResultMap() }.toList()
        return resultMaps.map { GenericDomValueReference(it) }.toTypedArray()
    }
}