package com.notification.notificationplatform.subscription

import jakarta.validation.constraints.NotNull

data class SubscriptionUpdateRequest(
    @field:NotNull
    val status: SubscriptionStatus
)
