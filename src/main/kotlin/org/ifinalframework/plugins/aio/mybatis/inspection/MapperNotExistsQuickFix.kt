package org.ifinalframework.plugins.aio.mybatis.inspection

import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.writeText
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.common.util.getBasePath
import org.ifinalframework.plugins.aio.intellij.GenericQuickFix
import org.ifinalframework.plugins.aio.resource.I18N
import org.ifinalframework.plugins.aio.service.NotificationService
import org.jetbrains.kotlin.idea.base.util.module
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
 */
class MapperNotExistsQuickFix(val clazz: UClass) : GenericQuickFix() {
    override fun applyFix(project: Project, problem: ProblemDescriptor) {
        val element = problem.psiElement
        val module = element.module ?: return

        createInModuleResource(module)

    }

    private fun createInModuleResource(module: Module) {
        val path = "${module.getBasePath()}/resources/${clazz.qualifiedName!!.substringBeforeLast('.').replace('.', '/')}"
        val fileName = "${clazz.name}.xml"
        File(path).mkdirs()
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(path)
        R.dispatch {
            val project = module.project
            val file = R.computeInWrite { Objects.requireNonNull<VirtualFile?>(virtualFile).createChildData(project, fileName) }
            // 写模板内容
            R.runInWrite {
                file?.writeText(
                    """
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${clazz.qualifiedName}">
</mapper>
                """.trimIndent()
                )
            }
            service<NotificationService>().notify(
                NotificationDisplayType.TOOL_WINDOW, "创建Mapper文件：$fileName", NotificationType.INFORMATION
            )
            val editorManager = FileEditorManager.getInstance(project)
            editorManager.openFile(file!!, true)
        }

    }

    override fun getName(): String {
        return I18N.message("MyBatis.MapperNotExistsQuickFix.name")
    }
}