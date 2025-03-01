package org.ifinalframework.plugins.aio.spring.provider;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.containers.stream
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import org.ifinalframework.plugins.aio.service.PsiService
import org.jetbrains.kotlin.idea.base.psi.kotlinFqName
import org.jetbrains.kotlin.idea.core.util.toPsiFile
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.uast.*


/**
 * Spring Cloud Feign 行标记，实现在`Controller`和`FeignClient`之间的快速跳转。
 *
 * @issue 28
 * @author iimik
 * @since 0.0.5
 **/
class SpringCloudFeignLineMarkerProvider : RelatedItemLineMarkerProvider() {

    private val jvmFileExtensions = listOf("java", "kt")

    private val mvcTooltip = I18N.message("Spring.SpringCloudFeignLineMarkerProvider.mvc.tooltip")
    private val feignTooltip = I18N.message("Spring.SpringCloudFeignLineMarkerProvider.feign.tooltip")

    override fun collectNavigationMarkers(element: PsiElement, result: MutableCollection<in RelatedItemLineMarkerInfo<*>>) {

        try {

            val u = element.toUElement() ?: return
            if (u is UIdentifier && u.uastParent is UMethod) {
                val uMethod = u.uastParent as UMethod
                val uClass = uMethod.getContainingUClass() ?: return

                val project = element.project
                val psiService = project.getService(PsiService::class.java)
                val apiMethodService = service<ApiMethodService>()

                if (uClass.isInterface && uClass.hasAnnotation(SpringAnnotations.FEIGN_CLIENT)) {
                    // @FeignClient
                    val apiMarker = apiMethodService.getApiMarker(element.parent) ?: return

//                val controllers = psiService.findAllClasses(ClassFilter { psiClass ->
//                    !psiClass.isInterface && psiClass.hasAnnotation(SpringAnnotations.REQUEST_MAPPING) && !psiClass.hasModifierProperty(PsiModifier.ABSTRACT)
//                })

                    val controllers = jvmFileExtensions.flatMap {
                        FilenameIndex.getAllFilesByExt(project, it)
                            .asSequence()
                            .filter { f -> f.name.endsWith("Controller.$it") }
                            .mapNotNull { f -> f.toPsiFile(project) }
                            .flatMap { f ->
                                PsiTreeUtil.findChildrenOfAnyType(f, PsiClass::class.java, KtClass::class.java)
                                    .filter { clazz ->
                                        val toUElement = clazz.toUElement()
                                        return@filter if (toUElement is UClass) {
                                            return@filter !toUElement.hasModifierProperty(PsiModifier.ABSTRACT)
                                                    && toUElement.hasAnnotation(SpringAnnotations.REQUEST_MAPPING)
                                                    && toUElement.name != null && toUElement.name!!.endsWith("Controller")
                                        } else false
                                    }
                                    .mapNotNull { f -> f.kotlinFqName?.asString() }
                            }
                            .mapNotNull { name -> psiService.findClass(name) }
                    }

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
                    val controllers = jvmFileExtensions.flatMap {
                        FilenameIndex.getAllFilesByExt(project, it)
                            .asSequence()
                            .filter { f -> f.name.endsWith("Client.$it") }
                            .mapNotNull { f -> f.toPsiFile(project) }
                            .flatMap { f ->
                                PsiTreeUtil.findChildrenOfAnyType(f, PsiClass::class.java, KtClass::class.java)
                                    .filter { clazz ->
                                        val toUElement = clazz.toUElement()
                                        return@filter if (toUElement is UClass) {
                                            return@filter toUElement.isInterface
                                                    && toUElement.hasAnnotation(SpringAnnotations.FEIGN_CLIENT)
                                                    && toUElement.name != null && toUElement.name!!.endsWith("Client")
                                        } else false
                                    }
                                    .mapNotNull { f -> f.kotlinFqName?.asString() }
                            }
                            .mapNotNull { name -> psiService.findClass(name) }
                    }

                    val methods = controllers.stream().flatMap { c -> c.allMethods.stream() }
                        .filter { method ->
                            val marker = apiMethodService.getApiMarker(method)
                            marker != null && marker.contains(apiMarker)
                        }
                        .toList()

                    if (methods.isNotEmpty()) {
                        val icon = AllIcons.Spring.CLOUD
                        val navigationGutterIconBuilder: NavigationGutterIconBuilder<PsiElement> = NavigationGutterIconBuilder.create(icon)
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