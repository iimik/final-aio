package org.ifinalframework.plugins.aio.api.yapi

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.util.Consumer
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.jetbrains.annotations.NonNls
import java.awt.event.MouseEvent
import javax.swing.Icon

/**
 * YApiStatusBarWidgetFactory
 *
 * @author iimik
 * @since 0.0.17
 */
class YApiStatusBarWidgetFactory : StatusBarWidgetFactory {
    override fun getId(): @NonNls String {
        return "yapi"
    }

    override fun getDisplayName(): @NlsContexts.ConfigurableName String {
        return "YApi"
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return MyStatusBarWidget()
    }

    override fun isEnabledByDefault(): Boolean {
        return true
    }

    private
    class MyStatusBarWidget : StatusBarWidget, StatusBarWidget.IconPresentation {
        private var myStatusBar: StatusBar? = null

        override fun ID(): String {
            return "Yapi"
        }

        override fun install(statusBar: StatusBar) {
            myStatusBar = statusBar
        }

        override fun getPresentation(): StatusBarWidget.WidgetPresentation {
            return this
        }

        override fun getTooltipText(): @NlsContexts.Tooltip String {
            return "Yapi"
        }

        override fun getClickConsumer(): Consumer<MouseEvent>? {
            return Consumer<MouseEvent> {
                val yapiService = myStatusBar!!.project!!.service<YapiService>()
                yapiService.refresh()
            }
        }

        override fun getShortcutText(): String? {
            return "YApi"
        }

        override fun getIcon(): Icon {
            return AllIcons.Api.YAPI
        }
    }
}