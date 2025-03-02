package org.ifinalframework.plugins.aio.mybatis.xml.converter

import com.intellij.openapi.components.service
import com.intellij.psi.PsiClass
import com.intellij.util.xml.ConvertContext
import com.intellij.util.xml.ResolvingConverter
import org.ifinalframework.plugins.aio.service.PsiService


/**
 * ClassResolvingConverter
 *
 * @author iimik
 * @since 0.0.6
 **/
class ClassResolvingConverter: ResolvingConverter<PsiClass>() {
    override fun toString(t: PsiClass?, context: ConvertContext): String? {
        return null
    }

    override fun fromString(clazz: String?, context: ConvertContext): PsiClass? {
        if(clazz == null) {
            return null
        }
        return context.project.service<PsiService>().findClass(clazz)
    }

    override fun getVariants(context: ConvertContext): MutableCollection<out PsiClass> {
        return mutableSetOf()
    }
}