package org.ifinalframework.plugins.aio.mybatis.provider;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlToken
import org.ifinalframework.plugins.aio.mybatis.service.StatementLineMarkerService
import org.ifinalframework.plugins.aio.resource.AllIcons


/**
 * Mapper Xml 行标记，快速跳转到 Java、Kotlin等JVM语言定义
 *
 * - mapper: 支持
 * - resultMap:
 * - insert:
 * - delete:
 * - update:
 * - select:
 *
 * @issue 26
 * @author iimik
 * @since 0.0.4
 **/
class StatementLineMarkerProvider : RelatedItemLineMarkerProvider() {

    private val logger = this.thisLogger();

    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {
        val statementLineMarkerService = element.project.getService(StatementLineMarkerService::class.java)
        if (element is XmlToken) {
            statementLineMarkerService.apply(element)?.let {
                logger.info(it.toString())
                if (it.targets == null) return@let
                val icon = AllIcons.Mybatis.XML
                val navigationGutterIconBuilder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(icon)
                navigationGutterIconBuilder.setTooltipText("跳转到Mapper")
                navigationGutterIconBuilder.setAlignment(GutterIconRenderer.Alignment.CENTER)
                navigationGutterIconBuilder.setTargets(it.targets!!)
                result.add(navigationGutterIconBuilder.createLineMarkerInfo(element))
            }
        }
    }
}