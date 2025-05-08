package org.ifinalframework.plugins.aio.api.yapi;

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intellij.openapi.components.service
import com.intellij.openapi.module.Module
import org.apache.commons.lang3.StringUtils
import org.ifinalframework.plugins.aio.api.yapi.model.Api
import org.ifinalframework.plugins.aio.api.yapi.model.CatMenu
import org.ifinalframework.plugins.aio.api.yapi.model.Project
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.Objects
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap


/**
 * DefaultYapiService
 *
 * @author iimik
 * @since 0.0.2
 **/
class DefaultYapiService(val project: com.intellij.openapi.project.Project) : YapiService {

    private val yapiClient: YapiClient
    private val projectCache: ConcurrentMap<Module, Project> = ConcurrentHashMap<Module, Project>()
    private val categoryCache: ConcurrentMap<Module, Map<String, CatMenu>> = ConcurrentHashMap<Module, Map<String, CatMenu>>()
    private val apiCache: ConcurrentMap<Module, Map<String, Api>> = ConcurrentHashMap<Module, Map<String, Api>>()

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

        return projectCache.computeIfAbsent(module) {
            yapiClient.getProject(yapiProperties.serverUrl + YapiClient.GET_PROJECT, token).execute().body()!!.data
        }

    }

    override fun getCatMenu(module: Module, category: String): CatMenu? {

        return categoryCache.computeIfAbsent(module){
            val project = getProject(module) ?: return@computeIfAbsent null

            val yapiProperties = module.project.service<YapiProperties>()
            if (StringUtils.isEmpty(yapiProperties.serverUrl)) {
                return@computeIfAbsent null
            }
            val token = yapiProperties.tokens[module.name] ?: return@computeIfAbsent null

            val result = yapiClient.getCatMenus(yapiProperties.serverUrl + YapiClient.GET_CAT_MENU, project.id, token)
                .execute().body()!!
            return@computeIfAbsent if (result.isSuccess()) {
                val categoryMap = mutableMapOf<String, CatMenu>()
                result.data!!.stream()
                    .filter{ Objects.nonNull(it) }
                    .forEach { categoryMap.put(it!!.name,it)}
                categoryMap
            } else null
        }?.get(category)


    }

    override fun getApi(module: Module, category: String, method: String, path: String): Api? {

        val apis = apiCache.computeIfAbsent(module) { m ->
            val project = getProject(m) ?: return@computeIfAbsent null

            val yapiProperties = m.project.service<YapiProperties>()
            if (StringUtils.isEmpty(yapiProperties.serverUrl)) {
                return@computeIfAbsent null
            }
            val token = yapiProperties.tokens[m.name] ?: return@computeIfAbsent null

            val result =
                yapiClient.getApiListInMenu(yapiProperties.serverUrl + YapiClient.GET_LIST_IN_MENU, token, project.id).execute().body()!!

            return@computeIfAbsent if (result.isSuccess()) {
                val apiMap = mutableMapOf<String, Api>()
                val categoryMap = mutableMapOf<String, CatMenu>()

                result.data.stream()
                    .peek { categoryMap.put(it.name, it) }
                    .flatMap { it.list?.stream() }
                    .forEach {
                        apiMap.put("${it.method} ${it.path}", it)
                    }

                categoryCache.put(module, categoryMap)

                apiMap

            } else emptyMap<String, Api>()
        }

        return apis["$method $path"]

    }
}