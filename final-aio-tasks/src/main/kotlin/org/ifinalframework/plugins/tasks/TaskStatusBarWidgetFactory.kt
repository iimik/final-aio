package org.ifinalframework.plugins.tasks

import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.tasks.TaskManager
import com.intellij.util.Consumer
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.service.NotificationService
import org.ifinalframework.plugins.aio.tasks.TasksIcons
import org.jetbrains.annotations.NonNls
import java.awt.event.MouseEvent
import javax.swing.Icon

/**
 * Task 状态栏组件
 *
 * 通过点击可强制刷新Tasks列表
 *
 * @author iimik
 */
class TaskStatusBarWidgetFactory : StatusBarWidgetFactory {
    override fun getId(): @NonNls String {
        return "Tasks"
    }

    override fun getDisplayName(): @NlsContexts.ConfigurableName String {
        return "Tasks"
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return MyStatusBarWidget(project)
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        val project = statusBar.project
        return if (project != null) {
            TaskManager.getManager(project).allRepositories.size > 0
        } else {
            super.canBeEnabledOn(statusBar)
        }
    }

    private
    class MyStatusBarWidget(val project: Project) : StatusBarWidget, StatusBarWidget.MultipleTextValuesPresentation {
        private val taskManager: TaskManager = TaskManager.getManager(project)
        private var myStatusBar: StatusBar? = null

        override fun ID(): String {
            return "Tasks"
        }

        override fun install(statusBar: StatusBar) {
            myStatusBar = statusBar
        }


        override fun getTooltipText(): @NlsContexts.Tooltip String {
            return "Tasks"
        }

        override fun getPresentation(): StatusBarWidget.WidgetPresentation {
            return this
        }

        override fun getClickConsumer(): Consumer<MouseEvent> {
            return Consumer<MouseEvent> {
                R.async {
                    try {
                        val issues = taskManager.getIssues(null, false)
                        service<NotificationService>().info("已刷新${issues.size}条Tasks！")
                    } catch (ex: Exception) {
                        thisLogger().error("刷新Tasks异常", ex)
                    }
                }
            }
        }

        override fun getShortcutText(): String {
            return "Tasks"
        }

        override fun getSelectedValue(): @NlsContexts.StatusBarText String {
            val localTasks = taskManager.localTasks
            val total = localTasks.size
            val active = localTasks.count { it.isActive }
            return "Tasks:${active}/${total}"
        }

        override fun getIcon(): Icon {
            return TasksIcons.TASK
        }
    }
}