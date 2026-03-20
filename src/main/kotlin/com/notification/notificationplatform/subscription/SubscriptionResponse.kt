package com.notification.notificationplatform.subscription

import com.notification.notificationplatform.event.Channel
import java.time.LocalDateTime

data class SubscriptionResponse(
    val subscriptionId: Long,
    val userId: String,
    val eventType: String,
    val channel: Channel,
    val webhookUrl: String?,
    val status: SubscriptionStatus,
    val createdAt: LocalDateTime
)
