package org.ifinalframework.plugins.aio.tasks

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.service.BrowserService
import java.awt.event.MouseEvent


/**
 * IssueLineMarkerProvider
 *
 * @issue 10
 * @author iimik
 * @since 0.0.1
 * @see JvmIssueLineMarkerProvider
 **/
class MarkdownIssueLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        try {


            val taskDoc = service<MarkdownTaskDocService>().getTaskDoc(element) ?: return null
            val builder = NavigationGutterIconBuilder.create(taskDoc.icon)
            builder.setTargets(element)
//            builder.setTooltipText(I18N.getMessage("${this::class.simpleName}.${issue.type.name.lowercase()}Tooltip"))
            return builder.createLineMarkerInfo(
                element
            ) { _: MouseEvent?, _: PsiElement? ->
                val url = service<TaskDocProcessor>().buildUrl(element.project, taskDoc)?: return@createLineMarkerInfo
                service<BrowserService>().open(url)
            }
        } catch (ex: ProcessCanceledException) {
            // ignore
            return null
        }
        return null
    }


}