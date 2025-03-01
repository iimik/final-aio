package org.ifinalframework.plugins.aio.mybatis.xml.converter

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.util.xml.ConvertContext
import com.intellij.util.xml.DomUtil
import com.intellij.util.xml.ResolvingConverter
import org.ifinalframework.plugins.aio.common.util.getService
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.ifinalframework.plugins.aio.service.PsiService
import java.util.*


/**
 * StatementMethodResolvingConverter
 *
 * @issue 31
 * @author iimik
 * @since 0.0.6
 **/
class StatementMethodResolvingConverter : ResolvingConverter<PsiMethod>() {
    override fun toString(t: PsiMethod?, context: ConvertContext): String? {
        return null
    }

    override fun fromString(id: String?, context: ConvertContext): PsiMethod? {
        this.thisLogger().info(id)
        val mapper = DomUtil.getParentOfType(context.invocationElement, Mapper::class.java, true) ?: return null
        val namespace = mapper.getNamespace().stringValue ?: return null
        val clazz = context.project.getService<PsiService>().findClass(namespace) ?: return null
        val methods = clazz.findMethodsByName(id, false)
        if (methods.isEmpty()) return null
        return methods.firstOrNull { !it.hasModifierProperty(PsiModifier.DEFAULT) }
    }

    override fun getVariants(context: ConvertContext): MutableCollection<out PsiMethod> {
        return Collections.emptyList()
    }
}