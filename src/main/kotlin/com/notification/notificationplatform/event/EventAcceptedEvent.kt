package com.notification.notificationplatform.event

data class EventAcceptedEvent(
    val eventId: Long,
    val eventType: String,
    val payload: String?
)
