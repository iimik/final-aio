package org.ifinalframework.plugins.aio.api.yapi;

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import org.apache.commons.lang3.StringUtils
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.api.yapi.model.Api
import org.ifinalframework.plugins.aio.api.yapi.model.CatMenu
import org.ifinalframework.plugins.aio.api.yapi.model.Project
import org.ifinalframework.plugins.aio.service.NotificationService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap


/**
 * DefaultYapiService
 *
 * @author iimik
 * @since 0.0.2
 **/
class DefaultYapiService(val project: com.intellij.openapi.project.Project) : YapiService {

    private val yapiClient: YApiClient
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

        yapiClient = retrofit.create(YApiClient::class.java)
    }

    override fun getProject(module: Module): Project? {
        val yapiProperties = module.project.service<YapiProperties>()
        if (StringUtils.isEmpty(yapiProperties.serverUrl)) {
            return null
        }
        val token = yapiProperties.tokens[module.name] ?: return null

        return projectCache.computeIfAbsent(module) {
            yapiClient.getProject(yapiProperties.serverUrl + YApiClient.GET_PROJECT, token).execute().body()!!.data
        }

    }

    override fun getCatMenu(module: Module, category: String): CatMenu? {
        return getCatMenu(module)?.get(category)
    }

    private fun getCatMenu(module: Module): Map<String, CatMenu>? = categoryCache.computeIfAbsent(module) {
        val yapiProperties = module.project.service<YapiProperties>()
        if (StringUtils.isEmpty(yapiProperties.serverUrl)) {
            return@computeIfAbsent null
        }
        val token = yapiProperties.tokens[module.name] ?: return@computeIfAbsent null

        val result = yapiClient.getCatMenus(yapiProperties.serverUrl + YApiClient.GET_CAT_MENU, token)
            .execute().body()!!
        return@computeIfAbsent if (result.isSuccess()) {
            val categoryMap = mutableMapOf<String, CatMenu>()
            result.data!!.stream()
                .filter { Objects.nonNull(it) }
                .forEach { categoryMap.put(it!!.name, it) }
            categoryMap
        } else null
    }

    override fun getApi(module: Module, category: String, method: String, path: String): Api? {
        val apis = getApis(module)
        return apis?.get("$method $path")
    }

    private fun getApis(module: Module): Map<String, Api>? = apiCache.computeIfAbsent(module) { m ->

        val yapiProperties = m.project.service<YapiProperties>()
        if (StringUtils.isEmpty(yapiProperties.serverUrl)) {
            return@computeIfAbsent null
        }
        val token = yapiProperties.tokens[m.name] ?: return@computeIfAbsent null

        val result =
            yapiClient.getApiList(yapiProperties.serverUrl + YApiClient.GET_API_LIST, token).execute().body()!!

        return@computeIfAbsent if (result.isSuccess()) {
            val apiMap = mutableMapOf<String, Api>()

            result.data.list.stream()
                .forEach {
                    apiMap.put("${it.method} ${it.path}", it)
                }


            apiMap

        } else emptyMap<String, Api>()
    }

    override fun refresh() {
        R.async {
            projectCache.clear()
            categoryCache.clear()
            apiCache.clear()

            val yapiProperties = project.service<YapiProperties>()
            ModuleManager.getInstance(project).modules.forEach { module ->
                val token = yapiProperties.tokens[module.name]
            }

            service<NotificationService>().notify(NotificationDisplayType.TOOL_WINDOW, "清除YApi缓存", NotificationType.INFORMATION)

        }
    }
}