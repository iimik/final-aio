package org.ifinalframework.plugins.aio.api.markdown

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * MarkdownProperties
 *
 * @author iimik
 * @since 0.0.15
 */
@Service(Service.Level.PROJECT)
@State(
    name = "org.ifinalframework.plugins.aio.api.markdown.ApiMarkdownProperties",
    storages = [Storage("final-aio.xml")]
)
class ApiMarkdownProperties(
    /**
     * 模板
     */
    var template: String = """
        # ${'$'}{NAME}
    """.trimIndent(),
) : PersistentStateComponent<ApiMarkdownProperties> {
    override fun getState(): ApiMarkdownProperties? {
        return this
    }

    override fun loadState(properties: ApiMarkdownProperties) {
        XmlSerializerUtil.copyBean<ApiMarkdownProperties>(properties, this)
    }
}