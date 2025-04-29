package org.ifinalframework.plugins.aio.api.yapi

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
import javax.swing.JComponent

/**
 * YApiConfigurable
 *
 * @author iimik
 */
class YApiConfigurable(val project: Project) : Configurable, Configurable.Beta {

    private var panel: DialogPanel? = null

    override fun getDisplayName(): @NlsContexts.ConfigurableName String? {
        return "YApi"
    }

    override fun createComponent(): JComponent? {

        val apiProperties = project.service<YapiProperties>()

        panel = panel {

            group("通用") {
                row {
                    textField()
                        .label("服务地址:")
                        .align(Align.FILL)
                        .bindText(
                            getter = { apiProperties.serverUrl ?: "" },
                            setter = {
                                apiProperties.serverUrl = it
                            }
                        )
                }.layout(RowLayout.LABEL_ALIGNED)
            }

            group("Tokens") {
                ModuleManager.getInstance(project).modules.map { it.name }
                    .sorted()
                    .forEach { moduleName ->

                        row {
                            textField()
                                .label("${moduleName}:")
                                .align(Align.FILL)
                                .bindText(
                                    getter = { apiProperties.tokens[moduleName] ?: "" },
                                    setter = {
                                        apiProperties.tokens[moduleName] = it
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