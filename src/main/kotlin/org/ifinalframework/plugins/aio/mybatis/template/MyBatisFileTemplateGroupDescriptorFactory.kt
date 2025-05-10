package org.ifinalframework.plugins.aio.mybatis.template

import com.intellij.ide.fileTemplates.FileTemplateDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory
import org.ifinalframework.plugins.aio.resource.AllIcons

/**
 * MyBatis Mapper XML 模板工厂
 *
 * @issue 41
 * @author iimik
 * @since 0.0.10
 * @see org.ifinalframework.plugins.aio.mybatis.inspection.MapperNotExistsQuickFix
 */
class MyBatisFileTemplateGroupDescriptorFactory : FileTemplateGroupDescriptorFactory {

    companion object {
        const val MYBATIS_MAPPER_XML_TEMPLATE: String = "MyBatis Mapper.xml"
    }

    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor("MyBatis", AllIcons.Mybatis.JVM)
        group.addTemplate(FileTemplateDescriptor(MYBATIS_MAPPER_XML_TEMPLATE, AllIcons.Mybatis.JVM))
        return group
    }

}