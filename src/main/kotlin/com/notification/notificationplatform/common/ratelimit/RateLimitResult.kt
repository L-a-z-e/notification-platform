package com.notification.notificationplatform.common.ratelimit

data class RateLimitResult(
    val allowed: Boolean,
    val remaining: Long,
    val retryAfterSeconds: Long = 0
)
