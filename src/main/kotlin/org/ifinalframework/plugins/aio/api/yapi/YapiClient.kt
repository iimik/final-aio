package org.ifinalframework.plugins.aio.api.yapi

import org.ifinalframework.plugins.aio.api.yapi.model.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * YapiClient
 * @author iimik
 * @see <a href="https://hellosean1025.github.io/yapi/openapi.html">yapi开放 api</a>
 * @since 0.0.1
 **/
@FeignClient(name = "yapi-client", url = "\${final.api.yapi.server-url}")
interface YapiClient {
    /**
     * 获取项目基本信息
     *
     * @param token 项目token
     * @return
     */
    @GetMapping("/api/project/get")
    fun getProject(@RequestParam("token") token: String): Result<Project?>

    /**
     * 获取项目分类菜单列表
     *
     * @param projectId 项目ID
     * @param token     项目token
     * @return
     */
    @GetMapping("/api/interface/getCatMenu")
    fun getCatMenus(
        @RequestParam("project_id") projectId: Long,
        @RequestParam("token") token: String
    ): Result<List<CatMenu?>?>

    /**
     * 获取某个分类下接口列表
     *
     * @param token 项目token
     * @param catId 分类id
     * @param page  当前页面
     * @param limit 每页数量，默认为10，如果不想要分页数据，可将 limit 设置为比较大的数字，比如 1000
     * @return
     */
    @GetMapping("/api/interface/list_cat")
    fun getApiListInCat(
        @RequestParam("token") token: String,
        @RequestParam("catid") catId: Long,
        @RequestParam("page") page: Int = 1,
        @RequestParam("limit") limit: Int = Int.MAX_VALUE
    ): Result<Page<Api>>

    /**
     * 获取接口菜单列表
     */
    @GetMapping("/api/interface/list_menu")
    fun getApiListInMenu(
        @RequestParam("token") token: String,
        @RequestParam("project_id") projectId: Long,
    ): Result<List<CatMenu>>
}