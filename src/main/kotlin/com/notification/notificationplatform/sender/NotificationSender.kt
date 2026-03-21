package com.notification.notificationplatform.sender

import com.notification.notificationplatform.event.Channel
import com.notification.notificationplatform.notification.Notification

interface NotificationSender {
    fun send(notification: Notification): SendResult
    fun supports(channel: Channel): Boolean
}