package org.ifinalframework.plugins.aio.api.open

import com.intellij.openapi.module.Module
import org.ifinalframework.plugins.aio.api.model.ApiMarker


/**
 * ApiOpener
 *
 * @author iimik
 * @since 0.0.1
 **/
interface ApiOpener {
    fun open(module: Module, apiMarker: ApiMarker)
}
