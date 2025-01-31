package org.ifinalframework.plugins.aio.api.markdown;

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import org.apache.commons.lang3.StringUtils
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.api.spi.SpringApiMethodService
import org.ifinalframework.plugins.aio.application.ElementHandler
import org.ifinalframework.plugins.aio.application.annotation.ElementApplication
import org.ifinalframework.plugins.aio.common.util.getBasePath
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.service.DefaultNotificationService
import org.ifinalframework.plugins.aio.service.NotificationService
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.util.*
import javax.annotation.Resource


/**
 * MarkdownOpenApplication
 *
 * @issue 1
 * @author iimik
 * @since 0.0.1
 **/
@ElementApplication(
    [
        DocService::class,
        SpringApiMethodService::class,
        DefaultApiMarkdownPathFormatter::class,
        DefaultNotificationService::class,
    ]
)
class MarkdownOpenApplication(
    private val apiMarkdownPathFormatter: ApiMarkdownPathFormatter,
    private val notificationService: NotificationService,
) : ElementHandler {

    private val logger = LoggerFactory.getLogger(MarkdownOpenApplication::class.java)

    @Resource
    private lateinit var project: Project

    @Resource
    private lateinit var module: Module

    @Resource
    private lateinit var apiMethodService: ApiMethodService


    override fun handle(element: PsiElement) {

        val apiMarker = apiMethodService.getApiMarker(element) ?: return

        val markdownFile = getMarkdownFile(apiMarker) ?: return

        val editorManager = FileEditorManager.getInstance(project)
        editorManager.openFile(markdownFile, true)
    }

    private fun getMarkdownFile(apiMarker: ApiMarker): VirtualFile? {

        val markdownPath = apiMarkdownPathFormatter.format(apiMarker)

        val files = R.Read.compute {
            FilenameIndex.getVirtualFilesByName("${apiMarker.name}.md",  GlobalSearchScope.everythingScope(project))
        } ?: return null
        var markdownFile = files.stream().filter { it: VirtualFile? -> it!!.path.endsWith(markdownPath) }.findFirst().orElse(null)


        if (markdownFile == null) {
            val path = StringUtils.substringBeforeLast(markdownPath, "/")
            val fileName = StringUtils.substringAfterLast(markdownPath, "/")
            try {
                markdownFile = createMarkdownFile(path, fileName)
                notificationService!!.notify(
                    NotificationDisplayType.TOOL_WINDOW, "创建Markdown文件：$fileName", NotificationType.INFORMATION
                )
            } catch (e: IOException) {
                logger.error("create markdown file error: {}", markdownPath, e)
            }
        }

        return markdownFile
    }

    private fun createMarkdownFile(path: String, fileName: String): VirtualFile? {
        val basePath: String = module.getBasePath()
        val filePath = "$basePath/$path"
        return R.Write.compute {
            File(filePath).mkdirs()
            val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(filePath)
            Objects.requireNonNull<VirtualFile?>(virtualFile).createChildData(project, fileName)
        }
    }
}