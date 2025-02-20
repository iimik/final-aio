package org.ifinalframework.plugins.aio.mybatis.xml.converter;

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.xml.ConvertContext
import com.intellij.util.xml.CustomReferenceConverter
import com.intellij.util.xml.GenericDomValue


/**
 * StatementMethodConverter
 *
 * @author iimik
 * @since 0.0.4
 **/
class StatementMethodConverter : ConverterAdaptor<PsiElement>(), CustomReferenceConverter<XmlAttributeValue> {

    override fun fromString(s: String?, context: ConvertContext): PsiElement? {

        context.project
        return super.fromString(s, context)
    }

    override fun createReferences(value: GenericDomValue<XmlAttributeValue>?, element: PsiElement?, context: ConvertContext?): Array<PsiReference> {
        return emptyArray()
    }
}