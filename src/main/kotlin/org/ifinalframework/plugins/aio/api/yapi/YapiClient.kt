package org.ifinalframework.plugins.aio.api.yapi

import org.ifinalframework.plugins.aio.api.yapi.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import java.net.URI

/**
 * YapiClient
 * @author iimik
 * @see <a href="https://hellosean1025.github.io/yapi/openapi.html">yapi开放 api</a>
 * @since 0.0.1
 **/
interface YapiClient {

    companion object {
        const val GET_PROJECT = "/api/project/get"
        const val GET_CAT_MENU = "/api/interface/getCatMenu"
        const val GET_LIST_IN_CAT = "/api/interface/list_cat"
        const val GET_LIST_IN_MENU = "/api/interface/list_menu"
        const val GET_API_LIST = " /api/interface/list"
    }

    /**
     * 获取项目基本信息
     *
     * @param token 项目token
     * @return
     */
    @GET
    fun getProject(@Url host: String, @Query("token") token: String): Call<Result<Project?>>


    /**
     * 获取项目分类菜单列表
     *
     * @param projectId 项目ID
     * @param token     项目token
     * @return
     */
    @GET
    fun getCatMenus(
        @Url host: String,
        @Query("token") token: String
    ): Call<Result<List<CatMenu?>?>>

    /**
     * 获取某个分类下接口列表
     *
     * @param token 项目token
     * @param catId 分类id
     * @param page  当前页面
     * @param limit 每页数量，默认为10，如果不想要分页数据，可将 limit 设置为比较大的数字，比如 1000
     * @return
     */
    @GET
    fun getApiListInCat(
        host: URI,
        @Query("token") token: String,
        @Query("catid") catId: Long,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = Int.MAX_VALUE
    ): Result<Page<Api>>

    /**
     * 获取接口菜单列表
     */
    @GET
    fun getApiListInMenu(
        @Url host: String,
        @Query("token") token: String,
        @Query("project_id") projectId: Long,
    ): Call<Result<List<CatMenu>>>

    @GET
    fun getApiList(
        @Url host: String,
        @Query("token") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = Int.MAX_VALUE
    ):Call<Result<Page<Api>>>
}