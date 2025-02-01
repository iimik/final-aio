package org.springframework.boot.autoconfigure.http;

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.web.servlet.server.Encoding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.env.Environment
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter


/**
 * HttpMessageConvertersAutoConfiguration
 *
 * @author iimik
 * @since 0.0.2
 **/
@AutoConfiguration(after = [GsonAutoConfiguration::class, JacksonAutoConfiguration::class, JsonbAutoConfiguration::class])
@Import(
    JacksonHttpMessageConvertersConfiguration::class,
    GsonHttpMessageConvertersConfiguration::class,
    JsonbHttpMessageConvertersConfiguration::class
)
open class CustomHttpMessageConvertersAutoConfiguration {

    @Bean
    open fun messageConverters(converters: ObjectProvider<HttpMessageConverter<*>?>): HttpMessageConverters {
        return HttpMessageConverters(converters.orderedStream().toList())
    }

    @Configuration(proxyBeanMethods = false)
    protected open class StringHttpMessageConverterConfiguration {
        @Bean
        open fun stringHttpMessageConverter(environment: Environment): StringHttpMessageConverter {
            val encoding = Binder.get(environment).bindOrCreate(
                "server.servlet.encoding",
                Encoding::class.java
            )
            val converter = StringHttpMessageConverter(encoding.charset)
            converter.setWriteAcceptCharset(false)
            return converter
        }
    }
}