package com.notification.notificationplatform.event

data class EventCreateRequest(
    val channel: Channel,
    val recipient: String,
    val message: String?,
    val sender: String?
)
