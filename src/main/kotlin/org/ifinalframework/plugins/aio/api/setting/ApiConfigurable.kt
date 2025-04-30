package org.ifinalframework.plugins.aio.api.setting

import com.intellij.openapi.components.service
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.NlsContexts
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.RowLayout
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import org.ifinalframework.plugins.aio.api.ApiProperties
import javax.swing.JComponent

/**
 * ApiConfigurable
 *
 * @see org.ifinalframework.plugins.aio.api.yapi.YApiConfigurable
 * @see org.ifinalframework.plugins.aio.api.markdown.MarkdownConfigurable
 * @author iimik
 * @since 0.0.15
 */
class ApiConfigurable(val project: Project) : Configurable, Configurable.Beta {

    private var panel: DialogPanel? = null

    override fun getDisplayName(): @NlsContexts.ConfigurableName String? {
        return "API"
    }

    override fun createComponent(): JComponent? {

        val apiProperties = project.service<ApiProperties>()

        panel = panel {

            group("通用") {
                row {
                    textField()
                        .label("权限:")
                        .align(Align.FILL)
                        .bindText(
                            getter = { apiProperties.securityAnnotation ?: "" },
                            setter = {
                                apiProperties.securityAnnotation = it
                            }
                        )
                }.layout(RowLayout.LABEL_ALIGNED)
            }

            group("Context Paths") {
                ModuleManager.getInstance(project).modules.map { it.name }
                    .sorted()
                    .forEach { moduleName ->

                        row {
                            textField()
                                .label("${moduleName}:")
                                .align(Align.FILL)
                                .bindText(
                                    getter = { apiProperties.contextPaths[moduleName] ?: "" },
                                    setter = {
                                        apiProperties.contextPaths[moduleName] = it
                                    }
                                )
                        }.layout(RowLayout.LABEL_ALIGNED)

                    }
            }

        }


        return panel
    }

    override fun isModified(): Boolean {
        return panel?.isModified() ?: false
    }

    override fun apply() {
        panel?.apply()
    }
}