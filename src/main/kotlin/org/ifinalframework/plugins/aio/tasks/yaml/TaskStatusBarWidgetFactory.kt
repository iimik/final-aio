package org.ifinalframework.plugins.aio.tasks.yaml

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
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.service.NotificationService
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
        return "Task"
    }

    override fun getDisplayName(): @NlsContexts.ConfigurableName String {
        return "Task"
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return MyStatusBarWidget(project)
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        val project = statusBar.project
        if (project != null) {
            return TaskManager.getManager(project).allRepositories.size > 0
        } else {
            return super.canBeEnabledOn(statusBar)
        }
    }

    private
    class MyStatusBarWidget(val project: Project) : StatusBarWidget, StatusBarWidget.IconPresentation {
        private var myStatusBar: StatusBar? = null

        override fun ID(): String {
            return "Task"
        }

        override fun install(statusBar: StatusBar) {
            myStatusBar = statusBar
        }


        override fun getTooltipText(): @NlsContexts.Tooltip String {
            return "Task"
        }

        override fun getPresentation(): StatusBarWidget.WidgetPresentation? {
            return this
        }

        override fun getClickConsumer(): Consumer<MouseEvent>? {
            return Consumer<MouseEvent> {
                R.async {
                    try {
                        val issues = TaskManager.getManager(project).getIssues(null, false)
                        service<NotificationService>().info("已刷新${issues.size}条Issues！")
                    } catch (ex: Exception) {
                        thisLogger().error("刷新Tasks异常", ex)
                    }
                }
            }
        }

        override fun getShortcutText(): String? {
            return "Task"
        }

        override fun getIcon(): Icon {
            return AllIcons.Task.TASK
        }
    }
}