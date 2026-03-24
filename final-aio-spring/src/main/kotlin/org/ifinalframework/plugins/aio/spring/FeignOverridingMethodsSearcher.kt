package org.ifinalframework.plugins.aio.spring;

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.searches.OverridingMethodsSearch
import com.intellij.util.Processor
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.toUElement


/**
 * Feign Client 重写方法查询器
 *
 * Feign Client用来调用HTTP接口（一般是Controller）定义的，所以从语义上来说，HTTP接口的定义可以看作是Feign Client 方法的实现。
 *
 * @author iimik
 * @since 0.0.25
 * @see OverridingMethodsSearch
 * @see com.intellij.psi.impl.search.JavaOverridingMethodsSearcher
 **/
class FeignOverridingMethodsSearcher : QueryExecutorBase<PsiMethod, OverridingMethodsSearch.SearchParameters>() {
    override fun processQuery(
        parameters: OverridingMethodsSearch.SearchParameters,
        processor: Processor<in PsiMethod>
    ) {
        val method = parameters.method.toUElement() as? UMethod ?: return
        if (!SpringUtils.isFeignMethod(method)) {
            return
        }

        val marker = service<ApiMethodService>().getApiMarker(method) ?: return

        this.thisLogger().info("FeignMethodGotoDeclarationHandler getApiMarker: $marker")

        val index = HttpEndPointIndexManager.getIndex(parameters.method.project)

        val key = HttpEndPointKey(marker.methods[0], marker.paths[0])

        val httpEndPoints = index.findControllerHttpEndPoint(key)

        if (httpEndPoints.isNotEmpty()) {
            httpEndPoints.forEach { httpEndPoint ->
                processor.process(httpEndPoint.element as PsiMethod)
            }
        }

    }

}