package org.ifinalframework.plugins.aio.api

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.XMap
import org.ifinalframework.plugins.aio.api.yapi.YapiProperties
import org.springframework.boot.context.properties.ConfigurationProperties


/**
 * ApiProperties
 *
 * @author iimik
 * @since 0.0.1
 **/
@Service(Service.Level.PROJECT)
@State(
    name = "org.ifinalframework.plugins.aio.api.ApiProperties",
    storages = [Storage("final-aio.xml")]
)
data class ApiProperties(
    /**
     * 权限注解
     */
    var securityAnnotation: String? = null,
    /**
     *
     */
    @XMap
    val contextPaths: MutableMap<String, String> = mutableMapOf(),
) : PersistentStateComponent<ApiProperties> {
    override fun getState(): ApiProperties? {
        return this
    }

    override fun loadState(properties: ApiProperties) {
        XmlSerializerUtil.copyBean<ApiProperties>(properties, this)
    }
}
