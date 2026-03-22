package com.notification.notificationplatform.event

data class EventCreateResult(
    val event: Event,
    val isDuplicate: Boolean = false
)
