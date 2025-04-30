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
 */
@Service(Service.Level.PROJECT)
@State(
    name = "org.ifinalframework.plugins.aio.api.markdown.MarkdownProperties",
    storages = [Storage("final-aio.xml")]
)
class MarkdownProperties(
    var template: String = """
        # ${'$'}{NAME}
    """.trimIndent(),
) : PersistentStateComponent<MarkdownProperties> {
    override fun getState(): MarkdownProperties? {
        return this
    }

    override fun loadState(properties: MarkdownProperties) {
        XmlSerializerUtil.copyBean<MarkdownProperties>(properties, this)
    }
}