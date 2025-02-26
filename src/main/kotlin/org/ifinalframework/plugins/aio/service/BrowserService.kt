package org.ifinalframework.plugins.aio.service


/**
 * 浏览器服务
 *
 * @author iimik
 * @since 0.0.1
 **/
interface BrowserService {
    /**
     * 打开指定的URL
     */
    fun open(url: String)
}