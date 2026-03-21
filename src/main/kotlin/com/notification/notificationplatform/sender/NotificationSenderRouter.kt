package com.notification.notificationplatform.sender

import com.notification.notificationplatform.notification.Notification
import org.springframework.stereotype.Component

@Component
class NotificationSenderRouter(
    private val senders: List<NotificationSender>
) {
    fun route(notification: Notification): SendResult {
        val sender = senders.find { it.supports(notification.channel) }
            ?: throw IllegalArgumentException("Unsupported channel: ${notification.channel}")

        return sender.send(notification)
    }
}