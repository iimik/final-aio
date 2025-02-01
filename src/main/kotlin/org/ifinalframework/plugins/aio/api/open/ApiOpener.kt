package org.ifinalframework.plugins.aio.api.open

import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.application.annotation.ImplementedBy


/**
 * ApiOpener
 *
 * @author iimik
 * @since 0.0.1
 **/
@ImplementedBy(YapiOpener::class)
interface ApiOpener {
    fun open(apiMarker: ApiMarker)
}
