package com.notification.notificationplatform.subscription

import com.notification.notificationplatform.event.Channel
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SubscriptionCreateRequest(
    @field:NotBlank
    val userId: String,

    @field:NotBlank
    val eventType: String,

    @field:NotNull
    val channel: Channel,

    val webhookUrl: String? = null
)
