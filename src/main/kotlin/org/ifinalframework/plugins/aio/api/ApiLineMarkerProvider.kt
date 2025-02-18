package org.ifinalframework.plugins.aio.api;

import com.github.iimik.finalaio.I18NBundle
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.api.markdown.MarkdownOpenApplication
import org.ifinalframework.plugins.aio.api.open.OpenApiApplication
import org.ifinalframework.plugins.aio.api.spi.SpringApiMethodService
import org.ifinalframework.plugins.aio.application.ElementApplication
import org.ifinalframework.plugins.aio.resource.AllIcons


/**
 * Api 行标记
 *
 * @author iimik
 * @since 0.0.2
 **/
class ApiLineMarkerProvider: LineMarkerProvider {

    private val apiMethodService = SpringApiMethodService()

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        return null
    }

    override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>, result: MutableCollection<in LineMarkerInfo<*>>) {
        elements.forEach { element ->
            apiMethodService.getApiMarker(element)?.apply {
                result.add(buildOpenApiLineMarkerInfo(element))
                result.add(buildOpenMarkdownLineMarkerInfo(element))
            }
        }
    }

    /**
     * @issue 16
     */
    private fun buildOpenApiLineMarkerInfo(element: PsiElement): LineMarkerInfo<*> {
        val builder = NavigationGutterIconBuilder.create(AllIcons.Api.VIEW)
        builder.setTargets(element)
        builder.setTooltipText("Open API")
        return builder.createLineMarkerInfo(element) { e, elt ->
            ElementApplication.run(OpenApiApplication::class, element)
        }
    }

    /**
     * @issue 14
     */
    private fun buildOpenMarkdownLineMarkerInfo(element: PsiElement): LineMarkerInfo<*> {
        val builder: NavigationGutterIconBuilder<PsiElement> =
            NavigationGutterIconBuilder.create(AllIcons.Api.MARKDOWN)
        builder.setTargets(element)
        builder.setTooltipText(I18NBundle.message("ApiMarkdownLineMarkerProvider.tooltip"))
        return builder.createLineMarkerInfo(
            element
        ) { mouseEvent, psiElement ->
            // #15 open markdown
            ElementApplication.run(MarkdownOpenApplication::class, psiElement)
        }
    }
}