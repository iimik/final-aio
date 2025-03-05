package org.ifinalframework.plugins.aio.mybatis.provider;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifier
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UIdentifier
import org.jetbrains.uast.toUElement


/**
 * ResultMap 实体类行标记，点击跳转到Mapper Xml文件。
 *
 * @issue 27
 * @author iimik
 * @since 0.0.4
 **/
class ResultMapLineMarkerProvider : RelatedItemLineMarkerProvider() {

    private val tooltip = I18N.message("Mybatis.ResultMapLineMarkerProvider.tooltip")

    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {

        val uElement = element.toUElement() ?: return

        if (uElement is UIdentifier && uElement.uastParent is UClass) {
            val uClass = uElement.uastParent as UClass
            if (!uClass.hasModifierProperty(PsiModifier.ABSTRACT)) {
                // 非抽象类
                val qualifiedName = uClass.qualifiedName ?: return
                val resultMaps = element.project.getService(MapperService::class.java).findResultMaps(qualifiedName)
                if (resultMaps.isNotEmpty()) {
                    val targets = resultMaps.stream().map { it.xmlTag }.toList()
                    val icon = AllIcons.Mybatis.JVM
                    val navigationGutterIconBuilder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(icon)
                    navigationGutterIconBuilder.setTooltipText(tooltip)
                    navigationGutterIconBuilder.setAlignment(GutterIconRenderer.Alignment.CENTER)
                    navigationGutterIconBuilder.setTargets(targets)
                    result.add(navigationGutterIconBuilder.createLineMarkerInfo(element))
                }
            }
        }
    }
}