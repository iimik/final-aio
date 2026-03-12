package org.ifinalframework.plugins.aio.service

import java.awt.Desktop
import java.net.URI


/**
 * DefaultBrowserService
 *
 * @author iimik
 * @since 0.0.1
 **/
class DefaultBrowserService : BrowserService {
    override fun open(url: String) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI(url))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}