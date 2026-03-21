package com.notification.notificationplatform.config

import com.notification.notificationplatform.sender.SendResult
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.core.IntervalFunction
import io.github.resilience4j.retry.RetryConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class ResilienceConfig {

    @Bean
    fun retryConfig(): RetryConfig = RetryConfig.custom<SendResult>()
        .maxAttempts(3)
        .intervalFunction(IntervalFunction.ofExponentialBackoff(1000, 2.0))
        .retryExceptions(RuntimeException::class.java)
        .ignoreExceptions(IllegalArgumentException::class.java)
        .build()

    @Bean
    fun circuitBreakerConfig(): CircuitBreakerConfig = CircuitBreakerConfig.custom()
        .slidingWindowSize(10)
        .failureRateThreshold(50.0f)
        .waitDurationInOpenState(Duration.ofSeconds(30))
        .permittedNumberOfCallsInHalfOpenState(3)
        .build()
}
