package org.ifinalframework.plugins.aio.mybatis.provider

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.mybatis.service.JvmMapperLineMarkerService
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.jetbrains.uast.UIdentifier
import org.jetbrains.uast.toUElement


/**
 * Mapper Line Marker，快速跳转到xml文件
 *
 * @issue 24
 * @author iimik
 * @since 0.0.4
 * @see [StatementLineMarkerProvider]
 **/
class MapperLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {


        val service = service<JvmMapperLineMarkerService>()
        service.apply(element)?.let {
            if (it.targets == null) return@let
            val icon = AllIcons.Mybatis.JVM
            val navigationGutterIconBuilder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(icon)
            navigationGutterIconBuilder.setTooltipText("跳转到Mapper")
            navigationGutterIconBuilder.setAlignment(GutterIconRenderer.Alignment.CENTER)
            navigationGutterIconBuilder.setTargets(it.targets)
            result.add(navigationGutterIconBuilder.createLineMarkerInfo(element))
        }
    }
}