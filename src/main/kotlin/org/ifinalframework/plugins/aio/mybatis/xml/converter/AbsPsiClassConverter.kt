package org.ifinalframework.plugins.aio.mybatis.xml.converter

import com.intellij.openapi.components.service
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.util.xml.ConvertContext
import com.intellij.util.xml.CustomReferenceConverter
import com.intellij.util.xml.GenericDomValue
import com.intellij.util.xml.PsiClassConverter
import org.ifinalframework.plugins.aio.service.PsiService


/**
 * ClassResolvingConverter
 *
 * @author iimik
 * @since 0.0.6
 **/
abstract class AbsPsiClassConverter : AbsConverter<PsiClass>(), CustomReferenceConverter<PsiClass> {

    protected val psiClassConverter = PsiClassConverter();

    override fun createReferences(p0: GenericDomValue<PsiClass>?, p1: PsiElement?, p2: ConvertContext?): Array<PsiReference> {
        return psiClassConverter.createReferences(p0, p1, p2)
    }
}