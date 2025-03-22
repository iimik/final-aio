package org.ifinalframework.plugins.aio.mybatis.template

import com.intellij.ide.fileTemplates.FileTemplateDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory
import org.ifinalframework.plugins.aio.resource.AllIcons

/**
 * MyBatis Mapper Xml 模板工厂
 *
 * @issue 41
 * @author iimik
 * @since 0.0.10
 */
class MyBatisFileTemplateGroupDescriptorFactory : FileTemplateGroupDescriptorFactory {

    companion object {
        private const val MYBATIS_MAPPER_XML_TEMPLATE: String = "MyBatis Mapper.xml"

    }

    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor("MyBatis", AllIcons.Mybatis.JVM)
        group.addTemplate(FileTemplateDescriptor(MYBATIS_MAPPER_XML_TEMPLATE, AllIcons.Mybatis.JVM))
        return group
    }

}