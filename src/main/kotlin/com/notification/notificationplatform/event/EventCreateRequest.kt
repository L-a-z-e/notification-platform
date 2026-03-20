package com.notification.notificationplatform.event

import jakarta.validation.constraints.NotBlank

data class EventCreateRequest(
    @field:NotBlank
    val source: String,

    @field:NotBlank
    val eventType: String,

    val payload: String?,
)
