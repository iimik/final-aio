package org.ifinalframework.plugins.aio.service

import org.ifinalframework.plugins.aio.application.annotation.ImplementedBy


/**
 * 浏览器服务
 *
 * @author iimik
 * @since 0.0.1
 **/
@ImplementedBy(DefaultBrowserService::class)
interface BrowserService {
    /**
     * 打开指定的URL
     */
    fun open(url: String)
}