package org.ifinalframework.plugins.aio.listener

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import org.ifinalframework.plugins.aio.service.EnvironmentService


/**
 * ConfigFileChangeListener
 *
 * @author iimik
 * @since 0.0.5
 **/
class ConfigFileChangeListener : BulkFileListener {

    private val logger by lazy { thisLogger() }

    override fun after(events: List<VFileEvent>) {

        events.filter { isConfigFile(it.file) }
            .forEach { event ->
                val file = event.file!!
                val name = file.name
                logger.info("after $name")

                ProjectUtil.getActiveProject()?.service<EnvironmentService>()?.reset()

            }
    }

    private fun isConfigFile(file: VirtualFile?): Boolean {
        return file != null && file.name == ".final.yml"
    }

}