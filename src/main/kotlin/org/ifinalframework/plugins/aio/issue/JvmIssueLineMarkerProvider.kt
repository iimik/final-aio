package org.ifinalframework.plugins.aio.issue

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.application.ElementApplication
import java.awt.event.MouseEvent


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
            val issue = service<JvmIssueService>().getIssue(element) ?: return null
            val builder = NavigationGutterIconBuilder.create(issue.type.icon)
            builder.setTargets(element)
            builder.setTooltipText(issue.type.toolTip)
            return builder.createLineMarkerInfo(
                element
            ) { _: MouseEvent?, _: PsiElement? ->
                element.project.service<IssueOpener>().open(issue)
            }
        } catch (ex: ProcessCanceledException) {
            // ignore
            return null
        }
        return null
    }


}