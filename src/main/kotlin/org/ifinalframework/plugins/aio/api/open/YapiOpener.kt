package org.ifinalframework.plugins.aio.api.open;

import org.ifinalframework.plugins.aio.api.ApiProperties
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.api.yapi.YapiService
import org.ifinalframework.plugins.aio.service.BrowserService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component


/**
 * 在浏览器中打开YAPI
 *
 * @issue 10
 * @author iimik
 * @since 0.0.1
 **/
@Component
@EnableConfigurationProperties(ApiProperties::class)
class YapiOpener(
    private val apiProperties: ApiProperties,
    private val yapiService: YapiService,
    private val browserService: BrowserService
) : ApiOpener {

    override fun open(apiMarker: ApiMarker) {

        val contextPath = apiProperties.contextPath?.trimEnd('/') ?: ""
        val methodPath = apiMarker.paths.first()?.trimStart('/')

        val path = "$contextPath/$methodPath"
        val api = yapiService.getApi(apiMarker.category, apiMarker.methods.first(), path) ?: return

        val url = "${apiProperties.yapi!!.serverUrl}/project/${api.projectId}/interface/api/${api.id}"
        browserService.open(url)
    }

}