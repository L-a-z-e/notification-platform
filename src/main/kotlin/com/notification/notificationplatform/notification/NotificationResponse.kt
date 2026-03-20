package com.notification.notificationplatform.notification

import com.notification.notificationplatform.event.Channel
import java.time.LocalDateTime

data class NotificationResponse(
    val notificationId: Long,
    val eventId: Long,
    val channel: Channel,
    val message: String,
    val status: NotificationStatus,
    val createdAt: LocalDateTime,
    val sentAt: LocalDateTime?
)
