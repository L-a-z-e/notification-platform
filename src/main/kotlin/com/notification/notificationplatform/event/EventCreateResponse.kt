package com.notification.notificationplatform.event

data class EventCreateResponse(
    val eventId: Long,
    val status: EventStatus
)
