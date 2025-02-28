package org.ifinalframework.plugins.aio.service

import com.intellij.ide.impl.ProjectUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.resource.AllIcons


/**
 * DefaultNotificationService
 *
 * @author iimik
 * @since 0.0.2
 **/
class DefaultNotificationService : NotificationService {
    override fun notify(displayType: NotificationDisplayType, content: String, notificationType: NotificationType) {
        val notification = Notification(NOTIFICATION_GROUP_PREFIX + displayType, content, notificationType)
        notification.icon = AllIcons.Plugin.LOGO
        R.dispatch { Notifications.Bus.notify(notification, ProjectUtil.getActiveProject()) }
    }

    companion object {
        const val NOTIFICATION_GROUP_PREFIX = "FINAL_AIO_"
    }
}