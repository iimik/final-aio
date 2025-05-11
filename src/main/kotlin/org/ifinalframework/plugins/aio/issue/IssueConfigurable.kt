package org.ifinalframework.plugins.aio.issue

import com.intellij.openapi.components.service
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
 * IssueConfigurable
 *
 * @author iimik
 */
class IssueConfigurable(private val project: Project) : Configurable, Configurable.Beta {

    private var panel: DialogPanel? = null

    override fun getDisplayName(): @NlsContexts.ConfigurableName String? {
        return "Issue"
    }

    override fun createComponent(): JComponent? {
        if (panel == null) {
            val properties = project.service<IssueProperties>()
            panel = panel {
                group("Jira") {
                    row {
                        textField()
                            .label("服务地址:")
                            .align(Align.FILL)
                            .bindText(
                                getter = { properties.jiraIssue.serverUrl ?: "" },
                                setter = {
                                    properties.jiraIssue.serverUrl = it
                                }
                            )
                    }.layout(RowLayout.LABEL_ALIGNED)
                    row {
                        textField()
                            .label("项目编码:")
                            .align(Align.FILL)
                            .bindText(
                                getter = { properties.jiraIssue.projectCode ?: "" },
                                setter = { properties.jiraIssue.projectCode = it }
                            )
                    }.layout(RowLayout.LABEL_ALIGNED)
                }
            }
        }
        return panel!!
    }

    override fun isModified(): Boolean {
        return panel!!.isModified()
    }

    override fun apply() {
        return panel!!.apply()
    }
}