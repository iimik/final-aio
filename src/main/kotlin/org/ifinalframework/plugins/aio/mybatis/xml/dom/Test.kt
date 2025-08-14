package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPrimitiveType
import com.intellij.psi.impl.source.PsiClassReferenceType
import com.intellij.util.xml.*
import org.ifinalframework.plugins.aio.mybatis.xml.converter.AbsConverter
import org.ifinalframework.plugins.aio.service.PsiService

/**
 * 对`<if>`和`<when>`标签中的`test`属性进行解析，避免调用（手误）不存在的属性（`field`）
 *
 * @author iimik
 */
interface Test : SqlFragment {
    @Required
    @NameValue
    @Attribute("test")
    @Convert(TestConverter::class)
    fun getTest(): GenericAttributeValue<List<PsiElement>>


    interface If : Test {}
    interface When : Test {}
    public class TestConverter : AbsConverter<List<PsiElement>>() {
        override fun fromString(test: String?, context: ConvertContext): List<PsiElement>? {
            if (test == null) return null
            val statement = DomUtil.getParentOfType(context.invocationElement, Statement::class.java, true) ?: return null
            val method = statement.getId().value ?: return null
            val parameters = method.parameterList.parameters

            if (parameters.isNullOrEmpty()) return null
            val properties = test.split(" ")

            if (parameters.size == 1) {
                val parameter = parameters[0]
                val type = parameter.type
                if (type is PsiClassReferenceType) {
                    val className = type.reference.qualifiedName
                    if (className.startsWith("java.lang.") || className.startsWith("java.util.")) {
                        if (properties.contains(parameter.name)) {
                            return listOf(parameter)
                        }
                    } else {
                        val project = context.project
                        val clazz = project.service<PsiService>().findClass(className) ?: return null
                        val fields = properties.mapNotNull { clazz.findFieldByName(it, true) }.toList()

                        if(fields.isEmpty()) return null

                        return fields;
                    }
                } else if (type is PsiPrimitiveType) {
                    if (properties.contains(parameter.name)) {
                        return listOf(parameter)
                    }
                }
            } else {
                val foundParameters = parameters.filter { properties.contains(it.name) }.toList()
                return foundParameters
            }

            return null


        }
    }


}