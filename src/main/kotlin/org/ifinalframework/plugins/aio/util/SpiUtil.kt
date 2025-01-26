package org.ifinalframework.plugins.aio.util;

import org.ifinalframework.plugins.aio.spi.LanguageSpiProxy
import java.lang.reflect.Proxy
import java.util.*
import kotlin.reflect.KClass


/**
 * SpiUtil
 *
 * @author iimik
 * @since 0.0.1
 **/
class SpiUtil {
    companion object {

        fun <T : Any> languageSpi(klz: KClass<T>): T {
            val services = ServiceLoader.load(klz.java, klz.java.classLoader).toList()
            val interfaces = arrayOf(klz.java)
            return Proxy.newProxyInstance(klz.java.classLoader, interfaces, LanguageSpiProxy(services)) as T
        }


    }
}
