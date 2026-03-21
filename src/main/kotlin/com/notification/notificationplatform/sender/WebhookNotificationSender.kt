package com.notification.notificationplatform.sender

import com.notification.notificationplatform.event.Channel
import com.notification.notificationplatform.notification.Notification
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class WebhookNotificationSender : NotificationSender {
    private val logger = LoggerFactory.getLogger(WebhookNotificationSender::class.java)

    override fun send(notification: Notification): SendResult {
        logger.info("Webhook 발송: userId={}, metadata={}, message={}", notification.userId, notification.metadata, notification.message)
        return SendResult.Success
    }

    override fun supports(channel: Channel): Boolean = channel == Channel.WEBHOOK
}
