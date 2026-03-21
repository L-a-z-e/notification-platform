package com.notification.notificationplatform.sender

import com.notification.notificationplatform.event.Channel
import com.notification.notificationplatform.notification.Notification
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SlackNotificationSender: NotificationSender {
    private val logger = LoggerFactory.getLogger(SlackNotificationSender::class.java)
    override fun send(notification: Notification): SendResult {
        if (notification.message.contains("[SIMULATE_FAILURE]")) {
            throw RuntimeException("Slack API 타임아웃")
        }
        logger.info("{} 발송: userId={}", notification.channel, notification.userId)
        return SendResult.Success
    }

    override fun supports(channel: Channel): Boolean = channel == Channel.SLACK
}