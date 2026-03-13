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
class MapperConverter : AbsConverter<PsiClass>(), CustomReferenceConverter<PsiClass> {

    private val psiClassConverter = PsiClassConverter();

    override fun fromString(clazz: String?, context: ConvertContext): PsiClass? {
        if (clazz == null) {
            return null
        }
        val psiClass = context.project.service<PsiService>().findClass(clazz) ?: return null
        return if (psiClass.isInterface) psiClass else null
    }

    override fun createReferences(p0: GenericDomValue<PsiClass>?, p1: PsiElement?, p2: ConvertContext?): Array<PsiReference> {
        return psiClassConverter.createReferences(p0, p1, p2)
    }
}