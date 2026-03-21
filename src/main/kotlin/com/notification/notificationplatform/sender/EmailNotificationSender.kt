package com.notification.notificationplatform.sender

import com.notification.notificationplatform.event.Channel
import com.notification.notificationplatform.notification.Notification
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EmailNotificationSender : NotificationSender {
    private val logger = LoggerFactory.getLogger(EmailNotificationSender::class.java)

    override fun send(notification: Notification): SendResult {
        logger.info("Email 발송: userId={}, message={}", notification.userId, notification.message)
        return SendResult.Success
    }

    override fun supports(channel: Channel): Boolean = channel == Channel.EMAIL
}
