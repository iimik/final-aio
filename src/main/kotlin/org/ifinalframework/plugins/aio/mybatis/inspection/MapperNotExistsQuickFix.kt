package org.ifinalframework.plugins.aio.mybatis.inspection

import com.google.common.base.CaseFormat
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.PopupStep
import com.intellij.openapi.ui.popup.util.BaseListPopupStep
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.writeText
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.common.util.getBasePath
import org.ifinalframework.plugins.aio.intellij.GenericQuickFix
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.mybatis.template.MyBatisFileTemplateGroupDescriptorFactory
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.ifinalframework.plugins.aio.service.NotificationService
import org.jetbrains.kotlin.idea.base.util.module
import org.jetbrains.kotlin.idea.util.sourceRoots
import org.jetbrains.uast.UClass
import java.io.File
import java.util.*

/**
 * 快速生成Mapper.xml文件
 *
 * ```xml
 * <?xml version="1.0" encoding="UTF-8"?>
 * <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
 * <mapper namespace="${MapperClassName}">
 * </mapper>
 * ```
 *
 * @author iimik
 * @since 0.0.10
 * @see MyBatisFileTemplateGroupDescriptorFactory
 */
class MapperNotExistsQuickFix(val clazz: UClass) : GenericQuickFix() {
    override fun applyFix(project: Project, problem: ProblemDescriptor) {
        val element = problem.psiElement
        val module = element.module ?: return
        val myBatisProperties = module.project.service<MyBatisProperties>()
        val mapperSourceRoots = getMapperSourceRoots(element, module, myBatisProperties)
        createMapperInSourceRoots(module, mapperSourceRoots, clazz, myBatisProperties)

    }

    private fun getMapperSourceRoots(element: PsiElement, module: Module, myBatisProperties: MyBatisProperties): List<VirtualFile> {
        val language = element.language.id.lowercase()

        val sourceRoots = module.sourceRoots
        return if (MyBatisProperties.MapperXmlPath.RESOURCE == myBatisProperties.mapperXmlPath) {
            return sourceRoots.filter { it.toString().contains("resources") }.toList()
        } else if (MyBatisProperties.MapperXmlPath.SOURCE == myBatisProperties.mapperXmlPath) {
            return sourceRoots.filter { it.toString().contains(language) }.toList()
        } else sourceRoots.toList()
    }

    private fun createMapperInSourceRoots(module: Module, roots: List<VirtualFile>, mapper: UClass, myBatisProperties: MyBatisProperties) {
        if (roots.size == 1) {
            createInSourceRoot(module, roots.first(), myBatisProperties)
        } else if (roots.size > 1) {
            val basePath = module.getBasePath() + "/"
            JBPopupFactory.getInstance().createListPopup(
                object : BaseListPopupStep<VirtualFile>(
                    I18N.message("MyBatis.MapperNotExistsQuickFix.sourceRootSelect.title"),
                    roots,
                    AllIcons.Mybatis.JVM
                ) {

                    override fun getTextFor(value: VirtualFile): String {
                        return value.toString().substringAfterLast(basePath)
                    }

                    override fun onChosen(sourceRoot: VirtualFile, finalChoice: Boolean): PopupStep<*>? {
                        createInSourceRoot(module, sourceRoot, myBatisProperties)
                        return PopupStep.FINAL_CHOICE
                    }
                }
            ).showInFocusCenter()
        }
    }

    private fun createInSourceRoot(module: Module, sourceRoot: VirtualFile, myBatisProperties: MyBatisProperties) {
        val project = module.project
        val path = "${sourceRoot.path}/${clazz.qualifiedName!!.substringBeforeLast('.').replace('.', '/')}"
        val fileName = "${clazz.name}.xml"
        File(path).mkdirs()
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(path)
        R.dispatch {
            val file = R.computeInWrite { Objects.requireNonNull<VirtualFile?>(virtualFile).createChildData(project, fileName) }
            val template = FileTemplateManager.getInstance(project)
                .getJ2eeTemplate(MyBatisFileTemplateGroupDescriptorFactory.MYBATIS_MAPPER_XML_TEMPLATE)
            val properties = Properties()
            properties.setProperty("NAMESPACE", clazz.qualifiedName)
            val content = template.getText(properties)
            // 写模板内容
            R.runInWrite { file?.writeText(content) }
            service<NotificationService>().notify(
                NotificationDisplayType.TOOL_WINDOW, "创建Mapper文件：$fileName", NotificationType.INFORMATION
            )

            val mapper = project.service<MapperService>().findMappers(clazz.qualifiedName!!).first()


            // table sql fragment
            val tableSqlFragment = myBatisProperties.tableSqlFragment
            if (tableSqlFragment.enable) {
                R.runInWriteAction(project) {
                    val sql = mapper.addSql()
                    sql.getId().stringValue = tableSqlFragment.id
                    val tableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.name!!.substringBeforeLast("Mapper"))
                    sql.setValue("${tableSqlFragment.prefix}${tableName}")
                }
            }


            val editorManager = FileEditorManager.getInstance(project)
            editorManager.openFile(file!!, true)
        }
    }


    override fun getName(): String {
        return I18N.message("MyBatis.MapperNotExistsQuickFix.name")
    }
}