package com.notification.notificationplatform.event

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class IdempotencyKeyRepository(
    private val redisTemplate: StringRedisTemplate
) {
    fun saveIfAbsent(idempotencyKey: String, ttl: Duration): Boolean {
        val result = redisTemplate.opsForValue()
            .setIfAbsent("idempotency:$idempotencyKey", "1", ttl)
        return result ?: false
    }
}