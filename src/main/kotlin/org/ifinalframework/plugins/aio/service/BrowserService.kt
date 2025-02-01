package org.ifinalframework.plugins.aio.service

import org.ifinalframework.plugins.aio.application.annotation.ImplementedBy


/**
 * BrowserService
 *
 * @author iimik
 * @since 0.0.1
 **/
@ImplementedBy(DefaultBrowserService::class)
interface BrowserService {
    fun open(url: String)
}