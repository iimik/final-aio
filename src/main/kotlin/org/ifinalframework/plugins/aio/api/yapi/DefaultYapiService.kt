package org.ifinalframework.plugins.aio.api.yapi;

import jakarta.annotation.Resource
import org.ifinalframework.plugins.aio.api.yapi.model.Api
import org.ifinalframework.plugins.aio.api.yapi.model.CatMenu
import org.ifinalframework.plugins.aio.api.yapi.model.Project
import org.springframework.stereotype.Service


/**
 * DefaultYapiService
 *
 * @author iimik
 * @since 0.0.2
 **/
@Service
class DefaultYapiService : YapiService {
    @Resource
    private lateinit var yapiProperties: YapiProperties

    @Resource
    private lateinit var yapiClient: YapiClient

    override fun getProject(): Project? {
        return yapiClient.getProject(yapiProperties.token!!).data
    }

    override fun getCatMenu(category: String): CatMenu? {
        val project = getProject() ?: return null
        val result = yapiClient.getCatMenus(project.id, yapiProperties.token!!)
        return if (result.isSuccess()) {
            result.data!!.stream()
                .filter { it!!.name == category }
                .findFirst().orElse(null)
        } else null
    }

    override fun getApi(category: String, method: String, path: String): Api? {
        val project = getProject() ?: return null
        val result = yapiClient.getApiListInMenu(yapiProperties.token!!, project.id)
        return if (result.isSuccess()) {
            result.data.stream()
                .flatMap { it.list?.stream() }
                .filter { it.method == method && it.path == path }
                .findFirst().orElse(null)
        } else null
    }
}