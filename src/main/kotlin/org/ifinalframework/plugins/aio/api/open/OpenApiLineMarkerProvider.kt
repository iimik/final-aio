package org.ifinalframework.plugins.aio.api.open;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.api.spi.SpringApiMethodService
import org.ifinalframework.plugins.aio.application.ElementApplication
import org.ifinalframework.plugins.aio.resource.AllIcons


/**
 * OpenApiLinerMarkerProvider
 * @issue 16
 * @author iimik
 * @since 0.0.2
 **/
class OpenApiLineMarkerProvider : LineMarkerProvider {

    private val apiMethodService = SpringApiMethodService()

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {

        val apiMarker = apiMethodService.getApiMarker(element) ?: return null

        val builder = NavigationGutterIconBuilder.create(AllIcons.Api.VIEW)
        builder.setTargets(element)
        builder.setTooltipText("Open API")
        return builder.createLineMarkerInfo(element, GutterIconNavigationHandler { e, elt ->
            ElementApplication.run(OpenApiApplication::class, element)
        })

    }
}