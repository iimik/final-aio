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
}