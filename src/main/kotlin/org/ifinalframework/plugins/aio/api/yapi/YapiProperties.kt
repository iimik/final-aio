package org.ifinalframework.plugins.aio.api.yapi

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.XMap


/**
 * YapiProperties
 *
 * @author iimik
 * @since 0.0.2
 **/
@Service(Service.Level.PROJECT)
@State(
    name = "org.ifinalframework.plugins.aio.api.yapi.YapiProperties",
    storages = [Storage("final-aio.xml")]
)
data class YapiProperties(
    var serverUrl: String? = null,
    @XMap
    var tokens: MutableMap<String, String> = mutableMapOf(),
) : PersistentStateComponent<YapiProperties> {
    override fun getState(): YapiProperties? {
        return this
    }

    override fun loadState(properties: YapiProperties) {
        XmlSerializerUtil.copyBean<YapiProperties>(properties, this)
    }
}
