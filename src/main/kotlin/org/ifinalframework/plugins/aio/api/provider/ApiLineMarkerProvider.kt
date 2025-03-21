package org.ifinalframework.plugins.aio.api.provider

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.api.open.OpenApiApplication
import org.ifinalframework.plugins.aio.api.service.MarkdownService
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.application.ElementApplication
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.ifinalframework.plugins.aio.service.EnvironmentService
import org.jetbrains.kotlin.idea.base.util.module
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

    private val logger = logger<ApiLineMarkerProvider>()

    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {
        try {
            val module = element.module ?: return
            val uElement = element.toUElement() ?: return
            if (uElement is UIdentifier) {
                val uClass = uElement.getContainingUClass()
                val environmentService = element.project.service<EnvironmentService>()
                val enable = environmentService.getProperty(module, "final.api.yapi.enable", Boolean::class, false)
                val apiMethodService = service<ApiMethodService>()
                apiMethodService.getApiMarker(element.parent)?.let {
                    logger.info("final.api.yapi.enable=$enable")
                    if (enable) {
                        // 仅当启用时才添加
                        result.add(buildOpenApiLineMarkerInfo(element))
                    }
                    // 忽略接口
                    if (uClass != null && !uClass.isInterface) {
                        collectMarkdownNavigationMarker(element, it, result)
                    }
                }
            }
        } catch (ex: ProcessCanceledException) {
            // ignore
        }
    }

    /**
     * @issue 16
     */
    private fun buildOpenApiLineMarkerInfo(element: PsiElement): RelatedItemLineMarkerInfo<*> {
        val builder = NavigationGutterIconBuilder.create(AllIcons.Api.YAPI)
        builder.setTargets(element)
        builder.setTooltipText("Open API")
        return builder.createLineMarkerInfo(element) { _, _ ->
            ElementApplication.run(OpenApiApplication::class, element.parent)
        }
    }

    /**
     * @issue 14
     */
    private fun collectMarkdownNavigationMarker(
        element: PsiElement,
        apiMarker: ApiMarker,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        service<MarkdownService>().findMarkdownFile(element.module!!, apiMarker)?.let {
            val builder: NavigationGutterIconBuilder<PsiElement> =
                NavigationGutterIconBuilder.create(AllIcons.Api.MARKDOWN)
            builder.setTargets(it)
            builder.setTooltipText(I18N.message("Api.ApiMarkdownLineMarkerProvider.tooltip"))
            val lineMarkerInfo = builder.createLineMarkerInfo(element)
            result.add(lineMarkerInfo)
        }
    }

}