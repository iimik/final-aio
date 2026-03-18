package org.ifinalframework.plugins.aio.spring.provider;

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesOptions
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifier
import com.intellij.usageView.UsageInfo
import com.intellij.util.Processor
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.service.PsiService
import org.ifinalframework.plugins.aio.spring.HttpEndPointIndexManager
import org.ifinalframework.plugins.aio.spring.HttpEndPointKey
import org.ifinalframework.plugins.aio.spring.service.SpringService
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getContainingUClass


/**
 * ControllerFindUsagesHandler
 *
 *
 * @author iimik
 * @since 0.0.25
 **/
class ControllerFindUsagesHandler(element: PsiElement) : FindUsagesHandler(element) {
    override fun processElementUsages(
        element: PsiElement,
        processor: Processor<in UsageInfo>,
        options: FindUsagesOptions
    ): Boolean {


        val project = element.project
        val springService = project.service<SpringService>()
        val psiService = project.service<PsiService>()
        val feignClientAnnotation = R.computeInRead { psiService.findClass(SpringAnnotations.FEIGN_CLIENT) }


        if (feignClientAnnotation == null) {
            return true
        }

        val uMethod = element as UMethod
        val uClass = uMethod.getContainingUClass() ?: return true

        val apiMethodService = service<ApiMethodService>()
        val isFeignClient = R.computeInRead { uClass.hasAnnotation(SpringAnnotations.FEIGN_CLIENT) }
        if (uClass.isInterface && isFeignClient == true) {
            // @FeignClient
            return true

        } else if (uClass.hasAnnotation(SpringAnnotations.REQUEST_MAPPING) && !uClass.hasModifierProperty(
                PsiModifier.ABSTRACT
            )
        ) {
            val apiMarker = apiMethodService.getApiMarker(uMethod) ?: return true

            val index = HttpEndPointIndexManager.getIndex(project)

            val httpEndPointKey = HttpEndPointKey(apiMarker.methods[0], apiMarker.paths[0])

            val httpEndPoints = index.findFeignHttpEndPoint(httpEndPointKey)

            if (httpEndPoints.isNotEmpty()) {

                for (httpEndPoint in httpEndPoints) {
                    processor.process(UsageInfo(httpEndPoint.element))
                }

                return true

            }


        } else {
            return false
        }

        return true
    }
}