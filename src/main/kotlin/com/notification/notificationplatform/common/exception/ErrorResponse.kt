package com.notification.notificationplatform.common.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.util.UUID

data class ErrorResponse(
    val error: String,
    val status: Int,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val path: String,
    val traceId: String = UUID.randomUUID().toString()
) {
}