package org.ifinalframework.plugins.aio.mybatis;

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.writeText
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiIdentifier
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.common.util.getBasePath
import org.jetbrains.kotlin.idea.base.psi.kotlinFqName
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassBody
import org.springframework.stereotype.Component
import java.io.File
import java.util.*


/**
 * DefaultMapperOpener
 *
 * @author iimik
 * @since 0.0.4
 **/
@Component
class DefaultMapperOpener(
    private val project: Project,
    private val module: Module
) : MapperOpener {
    override fun open(marker: MybatisMarker) {
        val element = marker.element
        if (element.language.id.equals("xml", ignoreCase = true)) {

        } else {
            openXml(marker)
        }
    }

    private fun openXml(marker: MybatisMarker) {
        var element = marker.element

        if (element is PsiIdentifier) {
            element = element.parent
        }

        val qualifiedName = when (element) {
            is KtClass -> element.fqName.toString()
            is KtClassBody -> element.kotlinFqName.toString()
            is PsiClass -> element.qualifiedName
            else -> null
        } ?: return

        val file = "${module.getBasePath()}/resources/${qualifiedName.replace('.', '/')}.xml"

        val localFileSystem = LocalFileSystem.getInstance()
        val virtualFile = localFileSystem.findFileByPath(file)
        val editorManager = FileEditorManager.getInstance(project)

        if (virtualFile == null) {
            val filePath = file.substringBeforeLast('/')
            val fileName = file.substringAfterLast('/')
            File(filePath).mkdirs()
            val virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(filePath)
            R.dispatch {
                val file = R.computeInWrite {
                    Objects.requireNonNull<VirtualFile?>(virtualFile).createChildData(project, fileName)?.apply {
                        writeText(
                            """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
                    <mapper namespace="$qualifiedName">
                    </mapper>
                """.trimIndent()
                        )
                    }
                }

                editorManager.openFile(file!!, true)
            }
        } else {
            R.dispatch { editorManager.openFile(virtualFile!!, true) }
        }

    }
}