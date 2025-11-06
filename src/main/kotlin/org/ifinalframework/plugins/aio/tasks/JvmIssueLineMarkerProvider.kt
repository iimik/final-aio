package org.ifinalframework.plugins.aio.tasks

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.service.BrowserService
import java.awt.event.MouseEvent
import java.util.concurrent.CancellationException


/**
 * IssueLineMarkerProvider
 *
 * @issue 10
 * @author iimik
 * @since 0.0.1
 * @see MarkdownIssueLineMarkerProvider
 **/
class JvmIssueLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        try {
            val taskDoc = service<JvmTaskDocService>().getTaskDoc(element) ?: return null
            val builder = NavigationGutterIconBuilder.create(taskDoc.icon)
            builder.setTargets(element)
            builder.setTooltipText(taskDoc.code)
            return builder.createLineMarkerInfo(
                element
            ) { _: MouseEvent?, _: PsiElement? ->
                val url = service<TaskDocProcessor>().buildUrl(element.project, taskDoc) ?: return@createLineMarkerInfo
                service<BrowserService>().open(url)
            }
        } catch (ex: CancellationException) {
            // ignore
            return null
        }
        return null
    }


}