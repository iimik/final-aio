package org.ifinalframework.plugins.aio.spring;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.jetbrains.uast.UIdentifier
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getContainingUClass
import org.jetbrains.uast.toUElement


/**
 * Feign Client 方法在语义上是 Controller 方法的使用方，
 *
 * @author iimik
 * @since 0.0.25
 * @see org.ifinalframework.plugins.aio.spring.provider.ControllerFindUsagesHandler
 **/
class FeignClientMethodGotoDeclarationHandler : GotoDeclarationHandler {
    override fun getGotoDeclarationTargets(
        element: PsiElement?,
        offset: Int,
        editor: Editor?
    ): Array<out PsiElement?>? {

        val u = element?.toUElement() ?: return null
        if (u is UIdentifier && u.uastParent is UMethod) {
            val klass = u.getContainingUClass() ?: return null
            val hasFeignClient = R.computeInRead { klass.hasAnnotation(SpringAnnotations.FEIGN_CLIENT) } ?: return null
            if (!hasFeignClient) {
                return null
            }
            val method = u.uastParent as UMethod

            val marker = service<ApiMethodService>().getApiMarker(method) ?: return null

            this.thisLogger().info("FeignMethodGotoDeclarationHandler getApiMarker: $marker")

            val index = HttpEndPointIndexManager.getIndex(element.project)

            val key = HttpEndPointKey(marker.methods[0], marker.paths[0])

            val httpEndPoints = index.findControllerHttpEndPoint(key) ?: return null

            if (httpEndPoints.isEmpty()) {
                return null
            }

            return httpEndPoints.map { it.element }
                .distinct()
                .toTypedArray()

        }

        return null
    }
}