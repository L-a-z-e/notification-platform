package com.notification.notificationplatform.subscription

import com.notification.notificationplatform.event.Channel

data class CachedSubscription(
    val userId: String,
    val channel: Channel,
    val webhookUrl: String?
)
