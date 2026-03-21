package com.notification.notificationplatform.sender

import com.notification.notificationplatform.event.Channel
import com.notification.notificationplatform.notification.Notification
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryConfig
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class NotificationEventListener(
    private val senderRouter: NotificationSenderRouter,
    private val processingService: NotificationProcessingService,
    private val retryConfig: RetryConfig,
    private val circuitBreakerConfig: CircuitBreakerConfig,
) {
    private val logger = LoggerFactory.getLogger(NotificationEventListener::class.java)

    private val slackCircuitBreaker = CircuitBreaker.of("slack", circuitBreakerConfig)
    private val webhookCircuitBreaker = CircuitBreaker.of("webhook", circuitBreakerConfig)
    @Async
    @EventListener
    fun handleNotificationCreatedEvent(event: NotificationCreatedEvent) {
        val notifications = processingService.getNotifications(event.notificationIds)

        notifications.forEach { notification ->
            var retryCount = 0
            val retry = Retry.of("notification-${notification.id}", retryConfig)

            retry.eventPublisher.onRetry { retryEvent ->
                retryCount = retryEvent.numberOfRetryAttempts
                logger.warn("재시도 {}/3: notificationId={}", retryEvent.numberOfRetryAttempts, notification.id)
            }

            try {
                val result = Retry.decorateSupplier(retry) {
                    sendWithCircuitBreaker(notification)
                }.get()

                when (result) {
                    is SendResult.Success -> processingService.updateSuccess(notification)
                    is SendResult.Failure -> processingService.updateFailure(notification, result.reason, retryCount)
                }
            } catch (e: Exception) {
                logger.error("최종 실패 → DLQ: notificationId={}, reason={}", notification.id, e.message)
                processingService.updateFailure(notification, e.message ?: "Unknown error", retryCount)
            }
        }
    }

    private fun sendWithCircuitBreaker(notification: Notification): SendResult {
        val circuitBreaker = when (notification.channel) {
            Channel.SLACK -> slackCircuitBreaker
            Channel.WEBHOOK -> webhookCircuitBreaker
            Channel.EMAIL -> null
        }

        return if (circuitBreaker != null) {
            CircuitBreaker.decorateSupplier(circuitBreaker) {
                senderRouter.route(notification)
            }.get()
        } else {
            senderRouter.route(notification)
        }
    }
}
