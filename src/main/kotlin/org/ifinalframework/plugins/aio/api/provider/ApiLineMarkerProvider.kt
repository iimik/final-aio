package org.ifinalframework.plugins.aio.api.provider

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiElement
import org.apache.commons.lang3.StringUtils
import org.ifinalframework.plugins.aio.api.ApiProperties
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.api.open.ApiOpener
import org.ifinalframework.plugins.aio.api.service.MarkdownService
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.api.yapi.YapiProperties
import org.ifinalframework.plugins.aio.api.yapi.YapiService
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.jetbrains.kotlin.idea.base.util.module
import org.jetbrains.uast.UIdentifier
import org.jetbrains.uast.getContainingUClass
import org.jetbrains.uast.toUElement
import kotlin.text.trimEnd


/**
 * Api 行标记
 *
 * @author iimik
 * @since 0.0.2
 **/
class ApiLineMarkerProvider : RelatedItemLineMarkerProvider() {

    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {
        try {
            val project = element.project
            val module = element.module ?: return
            val uElement = element.toUElement() ?: return
            if (uElement is UIdentifier) {
                val uClass = uElement.getContainingUClass()
                val yapiProperties = element.project.service<YapiProperties>()
                val enable = !StringUtils.isAnyBlank(yapiProperties.serverUrl, yapiProperties.tokens[module.name])
                val apiMethodService = service<ApiMethodService>()
                apiMethodService.getApiMarker(element.parent)?.let {
                    if (enable) {
                        val apiProperties = project.service<ApiProperties>()
                        val contextPath = apiProperties.contextPaths[module.name]?.trimEnd('/') ?: ""
                        val methodPath = it.paths.first().trimStart('/')

                        val path = "$contextPath/$methodPath"
                        val yapiService = project.service<YapiService>()

                        when(it.type){
                            ApiMarker.Type.METHOD -> yapiService.getApi(module, it.category, it.methods.first(), path)
                            ApiMarker.Type.CONTROLLER -> yapiService.getCatMenu(module, it.category)
                            else -> null
                        }?.let {
                            // 仅当启用时才添加
                            result.add(buildOpenApiLineMarkerInfo(element))
                        }


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

            val apiMarker = service<ApiMethodService>().getApiMarker(element.parent) ?: return@createLineMarkerInfo
            service<ApiOpener>().open(element.module!!, apiMarker)
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