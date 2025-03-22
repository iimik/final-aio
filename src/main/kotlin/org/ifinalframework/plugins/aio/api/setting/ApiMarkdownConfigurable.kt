package org.ifinalframework.plugins.aio.api.setting

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import org.ifinalframework.plugins.aio.api.ApiProperties
import org.ifinalframework.plugins.aio.resource.I18N
import javax.swing.JComponent

/**
 * ApiMarkdownConfigurable
 *
 * @author iimik
 */
class ApiMarkdownConfigurable(project: Project) : Configurable, Configurable.Beta {
    override fun createComponent(): JComponent? {

        val model = ApiProperties()

        return panel {
            row {
                checkBox(I18N.message("Api.ApiMarkdownConfigurable.checkbox.springmvc.label"))
                    .comment(I18N.message("Api.ApiMarkdownConfigurable.checkbox.springmvc.help"))
            }
            row {
                checkBox("Spring OpenFeign")
            }
            row {
                textField().label(I18N.message("Api.ApiMarkdownConfigurable.basePath")).bindText(model::markdownBasePath)
                    .comment(I18N.message("Api.ApiMarkdownConfigurable.basePath.help"))
            }
        }
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun apply() {
    }

    override fun getDisplayName(): String {
        return "Api Markdown"
    }
}