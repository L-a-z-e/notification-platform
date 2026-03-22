package com.notification.notificationplatform.common.ratelimit

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import tools.jackson.databind.ObjectMapper

@Component
class RateLimitInterceptor(
    private val rateLimiter: RateLimiter,
    private val objectMapper: ObjectMapper
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val clientId = request.getHeader("X-Client-Id") ?: request.remoteAddr

        val result = rateLimiter.isAllowed(clientId)

        response.setHeader("X-RateLimit-Remaining", result.remaining.toString())

        if (!result.allowed) {
            response.status = HttpStatus.TOO_MANY_REQUESTS.value()
            response.setHeader("Retry-After", result.retryAfterSeconds.toString())
            response.contentType = "application/json"
            response.writer.write(
                objectMapper.writeValueAsString(
                    mapOf(
                        "error" to "RATE_LIMIT_EXCEEDED",
                        "status" to 429,
                        "message" to "요청 한도를 초과했습니다",
                        "retryAfter" to result.retryAfterSeconds
                    )
                )
            )
            return false
        }

        return true
    }
}
