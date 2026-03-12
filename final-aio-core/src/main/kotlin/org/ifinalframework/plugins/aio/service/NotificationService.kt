package org.ifinalframework.plugins.aio.service

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationType
import org.ifinalframework.plugins.aio.application.annotation.EDT


/**
 * NotificationService
 *
 * @author iimik
 * @since 0.0.2
 **/
interface NotificationService {
    @EDT
    fun notify(displayType: NotificationDisplayType, content: String, notificationType: NotificationType)

    @EDT
    fun info(content: String){
        notify(NotificationDisplayType.TOOL_WINDOW, content, NotificationType.INFORMATION)
    }

    @EDT
    fun warn(content: String){
        notify(NotificationDisplayType.TOOL_WINDOW, content, NotificationType.WARNING)
    }

    @EDT
    fun error(content: String){
        notify(NotificationDisplayType.TOOL_WINDOW, content, NotificationType.ERROR)
    }
}