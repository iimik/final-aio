package org.ifinalframework.plugins.aio.setting

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

/**
 * FinalConfigurable
 *
 * - [org.ifinalframework.plugins.aio.api.setting.ApiConfigurable]
 *  - [org.ifinalframework.plugins.aio.api.yapi.YApiConfigurable]
 *  - [org.ifinalframework.plugins.aio.api.markdown.MarkdownConfigurable]
 *
 * @author iimik
 * @see [persistence](https://plugins.jetbrains.com/docs/intellij/persistence.html)
 */
class FinalConfigurable : Configurable {
    override fun createComponent(): JComponent? {
        return null
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun apply() {
    }

    override fun getDisplayName(): String {
        return "Final AIO"
    }
}