package org.ifinalframework.plugins.aio.spring.provider

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.searches.AnnotationTargetsSearch
import com.intellij.util.containers.stream
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.ifinalframework.plugins.aio.service.PsiService
import org.ifinalframework.plugins.aio.spring.service.SpringService
import org.jetbrains.uast.UIdentifier
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getContainingUClass
import org.jetbrains.uast.toUElement


/**
 * Spring Cloud Feign 行标记，实现在`Controller`和`FeignClient`之间的快速跳转。
 *
 * @issue 28
 * @author iimik
 * @since 0.0.5
 **/
class SpringCloudFeignLineMarkerProvider : RelatedItemLineMarkerProvider() {

    private val mvcTooltip = I18N.message("Spring.SpringCloudFeignLineMarkerProvider.mvc.tooltip")
    private val feignTooltip = I18N.message("Spring.SpringCloudFeignLineMarkerProvider.feign.tooltip")

    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {
        val project = element.project
        val springService = project.service<SpringService>()
        val psiService = project.service<PsiService>()
        val feignClientAnnotation = psiService.findClass(SpringAnnotations.FEIGN_CLIENT) ?: return
        try {

            val u = element.toUElement() ?: return
            if (u is UIdentifier && u.uastParent is UMethod) {
                val uMethod = u.uastParent as UMethod
                val uClass = uMethod.getContainingUClass() ?: return

                val apiMethodService = service<ApiMethodService>()

                if (uClass.isInterface && uClass.hasAnnotation(SpringAnnotations.FEIGN_CLIENT)) {
                    // @FeignClient

                    if (element.containingFile.virtualFile.toString().startsWith("jar://")) {
                        // ignore jar
                        return
                    }

                    val apiMarker = apiMethodService.getApiMarker(element.parent) ?: return

                    val controllers = springService.getResponseBodyAnnotations().flatMap {  AnnotationTargetsSearch.search(it) }
                        .filterIsInstance<PsiClass>()

                    val methods = controllers.stream().flatMap { c -> c.allMethods.stream() }
                        .filter { method ->
                            val marker = apiMethodService.getApiMarker(method)
                            marker != null && marker.contains(apiMarker)
                        }
                        .toList()

                    if (methods.isNotEmpty()) {
                        val icon = AllIcons.Spring.MVC
                        val navigationGutterIconBuilder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(icon)
                        navigationGutterIconBuilder.setTooltipText(feignTooltip)
                        navigationGutterIconBuilder.setAlignment(GutterIconRenderer.Alignment.CENTER)
                        navigationGutterIconBuilder.setTargets(methods)
                        result.add(navigationGutterIconBuilder.createLineMarkerInfo(element))
                    }

                } else if (uClass.hasAnnotation(SpringAnnotations.REQUEST_MAPPING) && !uClass.hasModifierProperty(PsiModifier.ABSTRACT)) {
                    val apiMarker = apiMethodService.getApiMarker(element.parent) ?: return

                    val feignClients = AnnotationTargetsSearch.search(feignClientAnnotation)
                        .filterIsInstance<PsiClass>()
                    val methods = feignClients.stream().flatMap { c -> c.allMethods.stream() }
                        .filter { method ->
                            val marker = apiMethodService.getApiMarker(method)
                            marker != null && marker.contains(apiMarker)
                        }
                        .toList()

                    if (methods.isNotEmpty()) {
                        val icon = AllIcons.Spring.CLOUD
                        val navigationGutterIconBuilder: NavigationGutterIconBuilder<PsiElement> =
                            NavigationGutterIconBuilder.create(icon)
                        navigationGutterIconBuilder.setTooltipText(mvcTooltip)
                        navigationGutterIconBuilder.setAlignment(GutterIconRenderer.Alignment.CENTER)
                        navigationGutterIconBuilder.setTargets(methods)
                        result.add(navigationGutterIconBuilder.createLineMarkerInfo(element))
                    }

                }

            }
        } catch (ex: ProcessCanceledException) {
            // ignore
        }
    }


}