package org.ifinalframework.plugins.aio.issue

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.application.ElementApplication
import org.ifinalframework.plugins.aio.resource.I18N
import org.ifinalframework.plugins.aio.util.SpiUtil
import java.awt.event.MouseEvent


/**
 * IssueLineMarkerProvider
 *
 * @issue 10
 * @author iimik
 * @since 0.0.1
 **/
class JvmIssueLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        try {
            val issue = service<JvmIssueService>().getIssue(element) ?: return null
            val builder = NavigationGutterIconBuilder.create(issue.type.icon)
            builder.setTargets(element)
            builder.setTooltipText(I18N.getMessage("${this::class.simpleName}.${issue.type.name.lowercase()}Tooltip"))
            return builder.createLineMarkerInfo(
                element
            ) { _: MouseEvent?, _: PsiElement? ->
                ElementApplication.run(OpenIssueApplication::class, element)
            }
        } catch (ex: ProcessCanceledException) {
            // ignore
            return null
        }
        return null
    }


}