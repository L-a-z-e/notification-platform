package com.notification.notificationplatform.event

data class EventCreateResponse(
    val eventId: Long,
    val status: String,
    val notificationCount: Int
)
