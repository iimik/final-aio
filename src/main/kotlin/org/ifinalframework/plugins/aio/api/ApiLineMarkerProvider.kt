package org.ifinalframework.plugins.aio.api

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.api.markdown.MarkdownOpenApplication
import org.ifinalframework.plugins.aio.api.open.OpenApiApplication
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.application.ElementApplication
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.jetbrains.uast.UIdentifier
import org.jetbrains.uast.getContainingUClass
import org.jetbrains.uast.toUElement


/**
 * Api 行标记
 *
 * @author iimik
 * @since 0.0.2
 **/
class ApiLineMarkerProvider : RelatedItemLineMarkerProvider() {

    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {
        val uElement = element.toUElement() ?: return
        if (uElement is UIdentifier) {
            val uClass = uElement.getContainingUClass()
            val apiMethodService = element.project.getService(ApiMethodService::class.java)
            apiMethodService.getApiMarker(element.parent)?.let {
                result.add(buildOpenApiLineMarkerInfo(element))
                // 忽略接口
                if (uClass != null && !uClass.isInterface) {
                    result.add(buildOpenMarkdownLineMarkerInfo(element))
                }
            }
        }
    }

    /**
     * @issue 16
     */
    private fun buildOpenApiLineMarkerInfo(element: PsiElement): RelatedItemLineMarkerInfo<*> {
        val builder = NavigationGutterIconBuilder.create(AllIcons.Api.VIEW)
        builder.setTargets(element)
        builder.setTooltipText("Open API")
        return builder.createLineMarkerInfo(element) { _, _ ->
            ElementApplication.run(OpenApiApplication::class, element.parent)
        }
    }

    /**
     * @issue 14
     */
    private fun buildOpenMarkdownLineMarkerInfo(element: PsiElement): RelatedItemLineMarkerInfo<*> {
        val builder: NavigationGutterIconBuilder<PsiElement> =
            NavigationGutterIconBuilder.create(AllIcons.Api.MARKDOWN)
        builder.setTargets(element)
        builder.setTooltipText(I18N.message("ApiMarkdownLineMarkerProvider.tooltip"))
        return builder.createLineMarkerInfo(
            element
        ) { _, _ ->
            // #15 open markdown
            ElementApplication.run(MarkdownOpenApplication::class, element.parent)
        }
    }
}