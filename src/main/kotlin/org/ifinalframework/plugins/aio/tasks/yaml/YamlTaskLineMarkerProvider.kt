package org.ifinalframework.plugins.aio.tasks.yaml

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.service.BrowserService
import org.ifinalframework.plugins.aio.tasks.TaskDocProcessor
import java.awt.event.MouseEvent

/**
 * YamlTaskLineMarkerProvider
 * 
 * @author iimik
 */
class YamlTaskLineMarkerProvider : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (element !is PsiComment) {
            return null
        }

        try {

            val taskDoc = service<YamlTaskDocService>().getTaskDoc(element) ?: return null
            val builder = NavigationGutterIconBuilder.create(taskDoc.icon)
            builder.setTargets(element)
//            builder.setTooltipText(I18N.getMessage("${this::class.simpleName}.${issue.type.name.lowercase()}Tooltip"))
            return builder.createLineMarkerInfo(
                element
            ) { _: MouseEvent?, _: PsiElement? ->
                val url = service<TaskDocProcessor>().buildUrl(element.project, taskDoc) ?: return@createLineMarkerInfo
                service<BrowserService>().open(url)
            }
        } catch (ex: ProcessCanceledException) {
            // ignore
            return null
        }

    }
}