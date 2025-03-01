package org.ifinalframework.plugins.aio.api.service

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.writeText
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.api.ApiConfigs
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.common.util.getBasePath
import org.ifinalframework.plugins.aio.service.EnvironmentService
import org.ifinalframework.plugins.aio.service.NotificationService
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownFile
import org.jetbrains.kotlin.idea.core.util.toPsiFile
import java.io.File
import java.util.*


/**
 * DefaultMarkdownService
 *
 * @author iimik
 * @since 0.0.6
 **/
class DefaultMarkdownService : MarkdownService {
    override fun findMarkdownFile(module: Module, marker: ApiMarker): MarkdownFile? {
        val environmentService = module.project.service<EnvironmentService>()
        val markdownBasePath = environmentService.getProperty(module, ApiConfigs.MarkdownBasePath)!!
        val markdownFilePath = "$markdownBasePath/${marker.category.replace("-", "/")}/${marker.name}.md"
        val path = "${module.getBasePath()}/${markdownFilePath}"
        val file = File(path)
        if (file.exists() && file.isFile) {
            val psiFile = file.toPsiFile(module.project)
            if (psiFile is MarkdownFile) {
                return psiFile
            }
        }

        return null;
    }

    override fun createMarkdownFile(module: Module, marker: ApiMarker) {
        val environmentService = module.project.service<EnvironmentService>()
        val markdownBasePath = environmentService.getProperty(module, ApiConfigs.MarkdownBasePath)!!
        val markdownFilePath = "${module.getBasePath()}/$markdownBasePath/${marker.category.replace("-", "/")}"
        val markdownFileName = "${marker.name}.md"
        File(markdownFilePath).mkdirs()
        val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(markdownFilePath)
        R.dispatch {
            val project = module.project
            val file = R.computeInWrite { Objects.requireNonNull<VirtualFile?>(virtualFile).createChildData(project, markdownFileName) }
            // 写模板内容
            R.runInWrite {
                file?.writeText("# ${marker.name}\n")
            }
            service<NotificationService>().notify(
                NotificationDisplayType.TOOL_WINDOW, "创建Markdown文件：$markdownFileName", NotificationType.INFORMATION
            )
            val editorManager = FileEditorManager.getInstance(project)
                editorManager.openFile(file!!, true)
        }
    }
}