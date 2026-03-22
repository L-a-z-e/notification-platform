package com.notification.notificationplatform.event

data class EventCreateResult(
    val event: Event? = null,
    val isDuplicate: Boolean = false
)
