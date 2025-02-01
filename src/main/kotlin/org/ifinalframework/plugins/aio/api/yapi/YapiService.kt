package org.ifinalframework.plugins.aio.api.yapi

import org.ifinalframework.plugins.aio.api.yapi.model.Api
import org.ifinalframework.plugins.aio.api.yapi.model.CatMenu
import org.ifinalframework.plugins.aio.api.yapi.model.Project


/**
 * YapiService
 *
 * @issue 10
 * @author iimik
 * @since 0.0.1
 **/
interface YapiService {
    fun getProject(): Project?
    fun getCatMenu(category: String): CatMenu?
    fun getApi(category: String, method: String, path: String): Api?
}