package org.ifinalframework.plugins.aio.service;

import com.intellij.notification.Notification
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import org.springframework.stereotype.Component


/**
 * DefaultNotificationService
 *
 * @author iimik
 * @since 0.0.2
 **/
@Component
class DefaultNotificationService : NotificationService {
    override fun notify(displayType: NotificationDisplayType, content: String, notificationType: NotificationType) {
        val notification = Notification(NOTIFICATION_GROUP_PREFIX + displayType, content, notificationType)
        Notifications.Bus.notify(notification)
    }

    companion object {
        const val NOTIFICATION_GROUP_PREFIX = "FINAL_AIO_";
    }
}