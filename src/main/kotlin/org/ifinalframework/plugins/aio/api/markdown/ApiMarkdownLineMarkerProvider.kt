package org.ifinalframework.plugins.aio.api.markdown;

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.api.spi.SpringApiMethodService
import org.ifinalframework.plugins.aio.resource.AllIcons


/**
 * Api Markdown 行标记
 *
 * @author iimik
 * @issue 14
 * @since 0.0.2
 **/
class ApiMarkdownLineMarkerProvider : LineMarkerProvider {

    private val apiMethodService = SpringApiMethodService()

    override fun getLineMarkerInfo(psiElement: PsiElement): LineMarkerInfo<*>? {

        val apiMarker = apiMethodService.getApiMarker(psiElement) ?: return null

        val builder: NavigationGutterIconBuilder<PsiElement> =
            NavigationGutterIconBuilder.create(AllIcons.Api.MARKDOWN)
        builder.setTargets(psiElement)
        builder.setTooltipText("打开Markdown文件")
        return builder.createLineMarkerInfo(
            psiElement
        ) { mouseEvent, psiElement ->
            // #15 TODO
        }

    }

}