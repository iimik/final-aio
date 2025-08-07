package org.ifinalframework.plugins.aio.mybatis.provider

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifier
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.service.MapperService
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.jetbrains.uast.*


/**
 * ResultMap 实体类行标记，点击跳转到Mapper Xml文件。
 *
 * @issue 27
 * @author iimik
 * @since 0.0.4
 * @see StatementLineMarkerProvider
 **/
@Suppress("UElementAsPsi")
class ResultMapLineMarkerProvider : RelatedItemLineMarkerProvider() {

    private val tooltip = I18N.message("MyBatis.ResultMapLineMarkerProvider.tooltip")

    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {

        val myBatisProperties = element.project.service<MyBatisProperties>()


        val targetElements = getTargetElements(element, myBatisProperties) ?: return
        if (targetElements.isEmpty()) return
        targetElements.let {
            val icon = AllIcons.Mybatis.JVM
            val navigationGutterIconBuilder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(icon)
            navigationGutterIconBuilder.setTooltipText(tooltip)
                .setAlignment(GutterIconRenderer.Alignment.RIGHT)
                .setTargets(targetElements)
            result.add(navigationGutterIconBuilder.createLineMarkerInfo(element))
        }

    }

    private fun getTargetElements(element: PsiElement, myBatisProperties: MyBatisProperties): List<PsiElement?>? {
        val uElement = element.toUElement() ?: return null

        if (uElement !is UIdentifier) return null

        val parent = uElement.uastParent
        return when (parent) {
            is UClass -> {
                val uClass = parent
                if (uClass.hasModifierProperty(PsiModifier.ABSTRACT)) return null
                val qualifiedName = uClass.qualifiedName ?: return null
                element.project.getService(MapperService::class.java).findResultMaps(qualifiedName)
                    .map { it.xmlTag }

            }

            is UField,
            is UParameter -> {

                if (myBatisProperties.lineMarker.resultMapProperty) {
                    val uField = parent
                    if (uField.hasModifierProperty(PsiModifier.STATIC)) return null
                    val uClass = uField.getContainingUClass() ?: return null
                    if (uClass.hasModifierProperty(PsiModifier.ABSTRACT)) return null
                    val qualifiedName = uClass.qualifiedName ?: return null
                    element.project.getService(MapperService::class.java).findResultMaps(qualifiedName)
                        .flatMap { it.getProperties() }
                        .filter { it.getProperty().stringValue == uField.name }
                        .map { it.getProperty().xmlAttributeValue }
                        .toList()

                } else null
            }

            else -> null
        }
    }

}
