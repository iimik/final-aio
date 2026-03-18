package org.ifinalframework.plugins.aio.spring

import com.intellij.database.util.common.asOptional
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AnnotationTargetsSearch
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.util.containers.stream
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.service.PsiService
import org.ifinalframework.plugins.aio.spring.service.SpringService


/**
 * HttpEndPointIndexManager
 *
 * @author iimik
 * @since 0.0.25
 **/
object HttpEndPointIndexManager {
    fun getIndex(project: Project): HttpEndPointIndex {
        return CachedValuesManager.getManager(project)
            .getCachedValue(project) {
                val index =
                    DumbService.getInstance(project)
                        .runReadActionInSmartMode<HttpEndPointIndex> {
                            buildIndex(project)
                        }

                CachedValueProvider.Result.create(
                    index,
                    PsiModificationTracker.MODIFICATION_COUNT
                )
            }
    }

    private fun buildIndex(project: Project): HttpEndPointIndex {
        val feign = mutableMapOf<HttpEndPointKey, MutableList<HttpEndPoint>>()
        val controller = mutableMapOf<HttpEndPointKey, MutableList<HttpEndPoint>>()
        val apiMethodService = service<ApiMethodService>()
        val scope = GlobalSearchScope.projectScope(project)


        thisLogger().info("Looking for @FeignClient...")
        project.service<PsiService>().findClass(SpringAnnotations.FEIGN_CLIENT).asOptional
            .ifPresent {
                val feignClients = AnnotationTargetsSearch.search(it, scope).filterIsInstance<PsiClass>()

                thisLogger().info("Looked ${feignClients.size} clients for @FeignClient")

                for (feignClient in feignClients) {
                    thisLogger().info("Starting parse ${feignClient.name} ...")

                    var methodCount = 0
                    for (method in feignClient.methods) {
                        val apiMarker = apiMethodService.getApiMarker(method)
                        if (apiMarker != null) {
                            methodCount++
                            val httpEndPoint = HttpEndPoint(apiMarker.methods[0], apiMarker.paths[0], method)
                            val key = HttpEndPointKey(apiMarker.methods[0], apiMarker.paths[0])
                            feign.getOrPut(key) { mutableListOf() }.add(httpEndPoint)
                        }
                    }

                    thisLogger().info("End parse ${feignClient.name}, parsed methods: $methodCount")
                }


            }

        thisLogger().info("Looking for @")
        val springService = project.service<SpringService>()
        val controllers = springService.getResponseBodyAnnotations().flatMap { AnnotationTargetsSearch.search(it) }
            .filterIsInstance<PsiClass>()

        controllers.stream().flatMap { c -> c.methods.stream() }
            .forEach { method ->
                val marker = apiMethodService.getApiMarker(method)
                if(marker != null) {
                    val httpEndPoint = HttpEndPoint(marker.methods[0], marker.paths[0], method)
                    val key = HttpEndPointKey(marker.methods[0], marker.paths[0])
                    controller.getOrPut(key) { mutableListOf() }.add(httpEndPoint)
                }
            }
        return HttpEndPointIndex(feign, controller)
    }


}