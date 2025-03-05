package org.ifinalframework.plugins.aio.mybatis.xml.converter

import com.intellij.openapi.components.service
import com.intellij.psi.PsiClass
import com.intellij.util.xml.ConvertContext
import org.ifinalframework.plugins.aio.mybatis.service.MybatisService
import org.ifinalframework.plugins.aio.resource.I18N

/**
 * TypeHandlerConverter
 *
 * @author iimik
 */
class TypeHandlerConverter : AbsPsiClassConverter() {

    override fun getErrorMessage(s: String?, context: ConvertContext): String? {
        return I18N.getMessage("MyBatis.TypeHandlerConverter.errorMessage", s)
    }

    override fun fromString(s: String?, context: ConvertContext): PsiClass? {
        val clazz = psiClassConverter.fromString(s, context) ?: return null
        val mybatisService = context.project.service<MybatisService>()
        return if (mybatisService.isTypeHandler(clazz)) clazz else null
    }
}