package org.ifinalframework.plugins.aio.issue;

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.diagnostic.logger
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
class IssueLineMarkerProvider : LineMarkerProvider {

    private val issueService = SpiUtil.languageSpi<IssueService>()
    private val logger = logger<IssueLineMarkerProvider>()

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {

        val issue = issueService.getIssue(element) ?: return null
        val builder = NavigationGutterIconBuilder.create(issue.type.icon)
        builder.setTargets(element)
        builder.setTooltipText(I18N.getMessage("${this::class.simpleName}.${issue.type.name.lowercase()}Tooltip"))
        return builder.createLineMarkerInfo(
            element
        ) { mouseEvent: MouseEvent?, element: PsiElement? ->
            ElementApplication.run(OpenIssueApplication::class, element!!)
        }
    }
}