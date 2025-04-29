package org.ifinalframework.plugins.aio.api.yapi

import com.intellij.openapi.module.Module
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
    fun getProject(module: Module): Project?
    fun getCatMenu(module: Module, category: String): CatMenu?
    fun getApi(module: Module, category: String, method: String, path: String): Api?
}