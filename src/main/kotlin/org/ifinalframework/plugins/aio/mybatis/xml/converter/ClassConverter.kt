package org.ifinalframework.plugins.aio.mybatis.xml.converter

import com.intellij.openapi.components.service
import com.intellij.psi.PsiClass
import com.intellij.util.xml.ConvertContext
import org.ifinalframework.plugins.aio.service.PsiService


/**
 * ClassResolvingConverter
 *
 * @author iimik
 * @since 0.0.6
 **/
class ClassConverter : AbsConverter<PsiClass>() {

    override fun fromString(clazz: String?, context: ConvertContext): PsiClass? {
        if (clazz == null) {
            return null
        }
        return context.project.service<PsiService>().findClass(clazz)
    }

}