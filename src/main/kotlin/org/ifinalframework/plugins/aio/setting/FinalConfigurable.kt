package org.ifinalframework.plugins.aio.setting

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

/**
 * FinalConfigurable
 *
 * @author iimik
 * @see [persistence](https://plugins.jetbrains.com/docs/intellij/persistence.html)
 */
class FinalConfigurable : Configurable, Configurable.Beta {
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