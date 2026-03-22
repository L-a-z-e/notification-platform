package com.notification.notificationplatform.common.ratelimit

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RateLimiter(
    private val redisTemplate: StringRedisTemplate,
    @Value("\${rate-limit.window-seconds}") private val windowSeconds: Long,
    @Value("\${rate-limit.max-requests}") private val maxRequests: Long
) {

    fun isAllowed(clientId: String): RateLimitResult {
        val key = "ratelimit:$clientId:${currentWindow()}"

        val count = redisTemplate.opsForValue().increment(key) ?: 1

        if (count == 1L) {
            redisTemplate.expire(key, Duration.ofSeconds(windowSeconds))
        }

        return if (count <= maxRequests) {
            RateLimitResult(allowed = true, remaining = maxRequests - count)
        } else {
            RateLimitResult(allowed = false, remaining = 0, retryAfterSeconds = ttl(key))
        }
    }

    private fun currentWindow(): Long {
        return System.currentTimeMillis() / (windowSeconds * 1000)
    }

    private fun ttl(key: String): Long {
        val ttl = redisTemplate.getExpire(key)
        return if (ttl > 0) ttl else windowSeconds
    }
}
