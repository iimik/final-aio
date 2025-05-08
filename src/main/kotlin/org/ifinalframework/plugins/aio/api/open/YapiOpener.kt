package org.ifinalframework.plugins.aio.api.open

import com.intellij.openapi.components.service
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import org.ifinalframework.plugins.aio.api.ApiProperties
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.api.yapi.YapiProperties
import org.ifinalframework.plugins.aio.api.yapi.YapiService
import org.ifinalframework.plugins.aio.api.yapi.model.CatMenu
import org.ifinalframework.plugins.aio.service.BrowserService


/**
 * 在浏览器中打开YAPI
 *
 * @issue 10
 * @author iimik
 * @since 0.0.1
 **/
class YapiOpener(
) : ApiOpener {

    override fun open(module: Module, apiMarker: ApiMarker) {

        val project = module.project

        when (apiMarker.type) {
            ApiMarker.Type.METHOD -> openApiMenu(project, module, apiMarker)
            ApiMarker.Type.CONTROLLER -> openCatMenu(project, module, apiMarker)
        }


    }

    private fun openCatMenu(project: Project, module: Module, apiMarker: ApiMarker) {
        val catMenu = project.service<YapiService>().getCatMenu(module, apiMarker.category) ?: return

        val serverUrl = project.service<YapiProperties>().serverUrl

        val url = "${serverUrl}/project/${catMenu.projectId}/interface/api/cat_${catMenu.id}"
        service<BrowserService>().open(url)
    }

    private fun openApiMenu(project: Project, module: Module, apiMarker: ApiMarker) {

        val apiProperties = project.service<ApiProperties>()
        val contextPath = apiProperties.contextPaths[module.name]?.trimEnd('/') ?: ""
        val methodPath = apiMarker.paths.first().trimStart('/')

        val path = "$contextPath/$methodPath"
        val api = project.service<YapiService>().getApi(module, apiMarker.category, apiMarker.methods.first(), path) ?: return

        val serverUrl = project.service<YapiProperties>().serverUrl

        val url = "${serverUrl}/project/${api.projectId}/interface/api/${api.id}"
        service<BrowserService>().open(url)
    }

}