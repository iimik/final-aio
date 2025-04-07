package org.ifinalframework.plugins.aio.mybatis

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

/**
 * MyBatisConfigurable
 *
 * @author iimik
 */
class MyBatisConfigurable(val project: Project) : Configurable, Configurable.Beta {

    val myBatisProperties: MyBatisProperties = project.service<MyBatisProperties>()

    val panel = panel {

        group("通用") {
            // Mapper Xml 路径
            buttonsGroup("Mapper XML 生成路径", indent = true) {
                row {
                    radioButton("ASK", MyBatisProperties.MapperXmlPath.ASK).enabled(false)
                    radioButton("RESOURCE", MyBatisProperties.MapperXmlPath.RESOURCE)
                    radioButton("SOURCE", MyBatisProperties.MapperXmlPath.SOURCE)
                }
            }
                .bind(object : MutableProperty<MyBatisProperties.MapperXmlPath> {
                    override fun get(): MyBatisProperties.MapperXmlPath {
                        return myBatisProperties.mapperXmlPath
                    }

                    override fun set(value: MyBatisProperties.MapperXmlPath) {
                        myBatisProperties.mapperXmlPath = value
                    }
                })

            // Table Sql Fragment
            row {
                val tableSqlFragment = myBatisProperties.tableSqlFragment
                checkBox("Table Sql Fragment")
                    .bindSelected(
                        getter = { tableSqlFragment.enable },
                        setter = { tableSqlFragment.enable = it }
                    )

                textField()
                    .label("Id:")
                    .align(Align.FILL)
                    .bindText(
                        getter = { tableSqlFragment.id },
                        setter = { tableSqlFragment.id = it }
                    )


                textField()
                    .label("Prefix:")
                    .align(Align.FILL)
                    .bindText(
                        getter = { tableSqlFragment.prefix },
                        setter = { tableSqlFragment.prefix = it }
                    )
            }

        }


        group(title = "Mapper Method Statement Type") {

            row {
                label("根据Method的名称推断对应的Statement类型")
            }

            row {
                textField()
                    .label("Insert:")
                    .align(Align.FILL)
                    .comment("Insert Statement 正则匹配")
                    .bindText(
                        getter = { myBatisProperties.insertMethodRegex },
                        setter = {
                            myBatisProperties.insertMethodRegex = it
                        }
                    )
            }

            row {
                textField()
                    .label("Delete:")
                    .align(Align.FILL)

                    .bindText(myBatisProperties::deleteMethodRegex)
            }

            row {
                textField()
                    .label("Update:")
                    .align(Align.FILL)
                    .bindText(myBatisProperties::updateMethodRegex)
            }

            row {
                textField()
                    .label("Select:")
                    .align(Align.FILL)
                    .bindText(myBatisProperties::selectMethodRegex)
            }
        }.layout(RowLayout.LABEL_ALIGNED)
    }

    override fun createComponent(): JComponent? {
        return panel
    }

    override fun isModified(): Boolean {
        return panel.isModified()
    }

    override fun apply() {
        panel.apply()
        this.thisLogger().info(myBatisProperties.toString())
    }

    override fun getDisplayName(): String {
        return "MyBatis"
    }
}