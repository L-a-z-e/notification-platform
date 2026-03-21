package com.notification.notificationplatform.event

data class EventCreateResult(
    val event: Event,
    val notificationCount: Int,
    val isDuplicate: Boolean = false
)
