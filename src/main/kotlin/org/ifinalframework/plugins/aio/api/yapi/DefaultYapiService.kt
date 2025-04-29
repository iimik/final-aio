package org.ifinalframework.plugins.aio.api.yapi;

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intellij.openapi.components.service
import com.intellij.openapi.module.Module
import org.apache.commons.lang3.StringUtils
import org.ifinalframework.plugins.aio.api.yapi.model.Api
import org.ifinalframework.plugins.aio.api.yapi.model.CatMenu
import org.ifinalframework.plugins.aio.api.yapi.model.Project
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.ServiceLoader


/**
 * DefaultYapiService
 *
 * @author iimik
 * @since 0.0.2
 **/
class DefaultYapiService(val project: com.intellij.openapi.project.Project) : YapiService {

    private val yapiClient: YapiClient

    init {
        val objectMapper = jacksonObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

        yapiClient = retrofit.create(YapiClient::class.java)
    }

    override fun getProject(module: Module): Project? {
        val yapiProperties = module.project.service<YapiProperties>()
        if (StringUtils.isEmpty(yapiProperties.serverUrl)) {
            return null
        }
        val token = yapiProperties.tokens[module.name] ?: return null
        return yapiClient.getProject(yapiProperties.serverUrl + YapiClient.GET_PROJECT, token).execute().body()!!.data
    }

    override fun getCatMenu(module: Module, category: String): CatMenu? {
        val project = getProject(module) ?: return null

        val yapiProperties = module.project.service<YapiProperties>()
        if (StringUtils.isEmpty(yapiProperties.serverUrl)) {
            return null
        }
        val token = yapiProperties.tokens[module.name] ?: return null

        val result = yapiClient.getCatMenus(yapiProperties.serverUrl + YapiClient.GET_CAT_MENU, project.id, token)
            .execute().body()!!
        return if (result.isSuccess()) {
            result.data!!.stream()
                .filter { it!!.name == category }
                .findFirst().orElse(null)
        } else null
    }

    override fun getApi(module: Module, category: String, method: String, path: String): Api? {
        val project = getProject(module) ?: return null

        val yapiProperties = module.project.service<YapiProperties>()
        if (StringUtils.isEmpty(yapiProperties.serverUrl)) {
            return null
        }
        val token = yapiProperties.tokens[module.name] ?: return null

        val result =
            yapiClient.getApiListInMenu(yapiProperties.serverUrl + YapiClient.GET_LIST_IN_MENU, token, project.id).execute().body()!!
        return if (result.isSuccess()) {
            result.data.stream()
                .flatMap { it.list?.stream() }
                .filter { it.method == method && it.path == path }
                .findFirst().orElse(null)
        } else null
    }
}