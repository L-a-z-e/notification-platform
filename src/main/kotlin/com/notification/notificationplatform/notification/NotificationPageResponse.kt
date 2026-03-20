package com.notification.notificationplatform.notification

data class NotificationPageResponse(
    val content: List<NotificationResponse>,
    val nextCursor: Long?,
    val hasNext: Boolean
)
