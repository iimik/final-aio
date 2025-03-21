package org.ifinalframework.plugins.aio.api.open

import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import jakarta.annotation.Resource
import org.ifinalframework.plugins.aio.api.ApiProperties
import org.ifinalframework.plugins.aio.api.spi.ApiMethodService
import org.ifinalframework.plugins.aio.api.yapi.DefaultYapiService
import org.ifinalframework.plugins.aio.api.yapi.YapiClient
import org.ifinalframework.plugins.aio.api.yapi.YapiProperties
import org.ifinalframework.plugins.aio.application.ElementHandler
import org.ifinalframework.plugins.aio.application.annotation.ElementApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.EnableFeignClients


/**
 * OpenApiApplication
 *
 * @author iimik
 * @since 0.0.2
 **/
@ElementApplication(
    [
        DefaultYapiService::class,
        ApiOpener::class
    ]
)
@EnableConfigurationProperties(ApiProperties::class, YapiProperties::class)
@EnableFeignClients(clients = [YapiClient::class])
class OpenApiApplication : ElementHandler {

    @Resource
    private lateinit var apiOpeners: List<ApiOpener>

    override fun handle(element: PsiElement) {
        val apiMarker = service<ApiMethodService>().getApiMarker(element) ?: return
        apiOpeners.forEach { it.open(apiMarker) }

    }

}