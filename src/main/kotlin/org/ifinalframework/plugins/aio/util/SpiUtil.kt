package org.ifinalframework.plugins.aio.util;

import org.ifinalframework.plugins.aio.spi.LanguageSpiProxy
import java.lang.reflect.Proxy
import java.util.*


/**
 * SpiUtil
 *
 * @author iimik
 * @since 0.0.1
 **/
object SpiUtil {

    inline fun <reified T : Any> languageSpi(): T {
        val clazz = T::class.java
        val loader = clazz.classLoader
        val services = ServiceLoader.load(clazz, loader).toList()
        val interfaces = arrayOf(clazz)
        return Proxy.newProxyInstance(loader, interfaces, LanguageSpiProxy(services)) as T
    }
}
