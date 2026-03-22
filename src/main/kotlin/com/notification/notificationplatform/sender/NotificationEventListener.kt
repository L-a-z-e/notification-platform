package com.notification.notificationplatform.sender

import com.notification.notificationplatform.event.Channel
import com.notification.notificationplatform.event.EventAcceptedEvent
import com.notification.notificationplatform.notification.Notification
import com.notification.notificationplatform.subscription.SubscriptionCacheService
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.core.IntervalFunction
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryConfig
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class NotificationEventListener(
    private val subscriptionCacheService: SubscriptionCacheService,
    private val notificationChunkService: NotificationChunkService,
    private val senderRouter: NotificationSenderRouter,
    private val processingService: NotificationProcessingService,
    private val objectMapper: ObjectMapper,
    private val retryConfig: RetryConfig,
    private val circuitBreakerConfig: CircuitBreakerConfig,
) {
    private val logger = LoggerFactory.getLogger(NotificationEventListener::class.java)

    private val slackCircuitBreaker = CircuitBreaker.of("slack", circuitBreakerConfig)
    private val webhookCircuitBreaker = CircuitBreaker.of("webhook", circuitBreakerConfig)
    private val CHUNK_SIZE = 1000

    private val statusUpdateRetryConfig = RetryConfig.custom<Unit>()
        .maxAttempts(3)
        .intervalFunction(IntervalFunction.ofExponentialBackoff(100, 2.0))
        .retryExceptions(ObjectOptimisticLockingFailureException::class.java)
        .build()

    @Async
    @EventListener
    fun handleEventAccepted(event: EventAcceptedEvent) {
        val subscriptions = subscriptionCacheService.findActiveSubscriptions(event.eventType)

        subscriptions.chunked(CHUNK_SIZE).forEachIndexed { index, chunk ->
            try {
                val notifications = chunk.map { subscription ->
                    Notification(
                        eventId = event.eventId,
                        userId = subscription.userId,
                        channel = subscription.channel,
                        message = event.payload ?: event.eventType,
                        metadata = if (subscription.channel == Channel.WEBHOOK)
                            objectMapper.writeValueAsString(mapOf("webhookUrl" to subscription.webhookUrl))
                        else null
                    )
                }

                val saved = notificationChunkService.saveChunk(notifications)
                saved.forEach { sendWithRetry(it) }
            } catch (e: Exception) {
                logger.error("청크 {} 처리 실패 — eventId={}, reason={}", index + 1, event.eventId, e.message)
            }
        }
    }

    private fun sendWithRetry(notification: Notification) {
        var retryCount = 0
        val sendRetry = Retry.of("send-${notification.id}", retryConfig)

        sendRetry.eventPublisher.onRetry { retryEvent ->
            retryCount = retryEvent.numberOfRetryAttempts
            logger.warn("발송 재시도 {}/3: notificationId={}", retryEvent.numberOfRetryAttempts, notification.id)
        }

        try {
            val result = Retry.decorateSupplier(sendRetry) {
                sendWithCircuitBreaker(notification)
            }.get()

            when (result) {
                is SendResult.Success -> retryStatusUpdate(notification.id!!) {
                    processingService.updateSuccess(it)
                }
                is SendResult.Failure -> retryStatusUpdate(notification.id!!) {
                    processingService.updateFailure(it, result.reason, retryCount)
                }
            }
        } catch (e: Exception) {
            logger.error("최종 발송 실패 → DLQ: notificationId={}, reason={}", notification.id, e.message)
            retryStatusUpdate(notification.id!!) {
                processingService.updateFailure(it, e.message ?: "Unknown error", retryCount)
            }
        }
    }

    private fun retryStatusUpdate(notificationId: Long, action: (Long) -> Unit) {
        val retry = Retry.of("status-$notificationId", statusUpdateRetryConfig)

        retry.eventPublisher.onRetry { event ->
            logger.warn("상태 업데이트 재시도 {}/3: notificationId={}", event.numberOfRetryAttempts, notificationId)
        }

        try {
            Retry.decorateRunnable(retry) {
                action(notificationId)
            }.run()
        } catch (e: ObjectOptimisticLockingFailureException) {
            logger.error("상태 업데이트 재시도 초과: notificationId={}", notificationId)
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
