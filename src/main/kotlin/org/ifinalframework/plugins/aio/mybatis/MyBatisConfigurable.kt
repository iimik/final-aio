package org.ifinalframework.plugins.aio.mybatis

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.*
import org.ifinalframework.plugins.aio.resource.I18N
import javax.swing.JComponent

/**
 * MyBatisConfigurable
 *
 * @author iimik
 */
class MyBatisConfigurable(val project: Project) : Configurable {

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

        }.layout(RowLayout.LABEL_ALIGNED)

        // test 补全提示配置
        group("Test Completion") {

            val testCompletion = myBatisProperties.testCompletion

            row {
                label("配置不同类型的补全表达式，可以使用\${TARGET}来表示属性")
            }

            row {
                textField()
                    .label(I18N.message("MyBatis.MyBatisConfigurable.testCompletion.String.label") + ":")
                    .align(Align.FILL)
                    .bindText(
                        getter = { testCompletion.stringType },
                        setter = {
                            testCompletion.stringType = it
                        }
                    )
            }.layout(RowLayout.LABEL_ALIGNED)

            row {
                textField()
                    .label(I18N.message("MyBatis.MyBatisConfigurable.testCompletion.Collection.label") + ":")
                    .align(Align.FILL)
                    .bindText(
                        getter = { testCompletion.collectionType },
                        setter = { testCompletion.collectionType = it }
                    )
            }.layout(RowLayout.LABEL_ALIGNED)

            row {
                textField()
                    .label(I18N.message("MyBatis.MyBatisConfigurable.testCompletion.Between.label") + ":")
                    .align(Align.FILL)
                    .bindText(
                        getter = { testCompletion.betweenType },
                        setter = { testCompletion.betweenType = it }
                    )
                    .comment(I18N.message("MyBatis.MyBatisConfigurable.testCompletion.Between.comment"))
            }.layout(RowLayout.LABEL_ALIGNED)

            row {
                textField()
                    .label(I18N.message("MyBatis.MyBatisConfigurable.testCompletion.Default.label") + ":")
                    .align(Align.FILL)
                    .bindText(
                        getter = { testCompletion.defaultType },
                        setter = {
                            testCompletion.defaultType = it
                        }
                    )
            }.layout(RowLayout.LABEL_ALIGNED)
        }.layout(RowLayout.LABEL_ALIGNED)


        group(title = "Statement Completion") {

            val statementMethodCompletion = myBatisProperties.statementMethodCompletion

            row {
                label("约定Method和Statement之间的映射关系")
            }

            row {
                textField()
                    .label("Insert:")
                    .align(Align.FILL)
                    .comment("Insert Statement 正则匹配")
                    .bindText(statementMethodCompletion::insertMethodRegex)
            }.layout(RowLayout.LABEL_ALIGNED)

            row {
                textField()
                    .label("Delete:")
                    .align(Align.FILL)

                    .bindText(statementMethodCompletion::deleteMethodRegex)
            }.layout(RowLayout.LABEL_ALIGNED)

            row {
                textField()
                    .label("Update:")
                    .align(Align.FILL)
                    .bindText(statementMethodCompletion::updateMethodRegex)
            }.layout(RowLayout.LABEL_ALIGNED)

            row {
                textField()
                    .label("Select:")
                    .align(Align.FILL)
                    .bindText(statementMethodCompletion::selectMethodRegex)
            }.layout(RowLayout.LABEL_ALIGNED)

            row {
                checkBox("启用正则方法过滤")
                    .comment("开启时，只有满足正则的方法才会出现在补全提示中")
                    .bindSelected(statementMethodCompletion::filterWithRegex)
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