package org.ifinalframework.plugins.aio.service


/**
 * EnvironmentService
 *
 * @author iimik
 * @since 0.0.5
 **/
interface EnvironmentService {
    fun getProperty(key: String): String?
}