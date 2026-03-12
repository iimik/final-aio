package org.ifinalframework.plugins.aio.api.spi

import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.api.model.ApiMarker


/**
 * ApiMethodService
 *
 * @author iimik
 * @since 0.0.2
 * @see SpringApiMethodService
 **/
@FunctionalInterface
interface ApiMethodService {
    fun getApiMarker(element: PsiElement): ApiMarker?

    companion object {
        fun getInstance(): ApiMethodService {
            return service<ApiMethodService>()
        }
    }
}