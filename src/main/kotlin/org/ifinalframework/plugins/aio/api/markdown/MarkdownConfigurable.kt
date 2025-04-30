package org.ifinalframework.plugins.aio.api.markdown

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.rows
import org.ifinalframework.plugins.aio.resource.I18N
import javax.swing.JComponent

/**
 * MarkdownConfigurable
 *
 * @author iimik
 */
class MarkdownConfigurable(val project: Project) : Configurable, Configurable.Beta {

    private var panel: DialogPanel? = null

    override fun getDisplayName(): @NlsContexts.ConfigurableName String? {
        return "Markdown"
    }

    override fun createComponent(): JComponent? {

        val markdownProperties = project.service<MarkdownProperties>()

        panel = panel {
            group(I18N.message("Api.MarkdownConfigurable.template.label")) {
                row {

                    textArea()
                        .align(Align.FILL)
                        .bindText(
                            getter = { markdownProperties.template ?: "" },
                            setter = {
                                markdownProperties.template = it
                            }
                        ).rows(10)
                        .comment(I18N.message("Api.MarkdownConfigurable.template.comment"))

                }
            }
        }
        return panel
    }

    override fun isModified(): Boolean {
        return panel!!.isModified()
    }

    override fun apply() {
        panel!!.apply()
    }
}