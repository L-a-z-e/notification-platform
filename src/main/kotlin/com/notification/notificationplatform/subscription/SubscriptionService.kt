package com.notification.notificationplatform.subscription

import com.notification.notificationplatform.common.exception.DuplicateResourceException
import com.notification.notificationplatform.common.exception.ResourceNotFoundException
import com.notification.notificationplatform.event.Channel
import org.springframework.stereotype.Service

@Service
class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository,
    private val subscriptionCacheService: SubscriptionCacheService
) {

    fun createSubscription(request: SubscriptionCreateRequest): Subscription {
        subscriptionRepository.findByUserIdAndEventTypeAndChannel(
            request.userId, request.eventType, request.channel
        )?.let { throw DuplicateResourceException("이미 존재하는 구독입니다") }

        if (request.channel == Channel.WEBHOOK && request.webhookUrl.isNullOrBlank()) {
            throw IllegalArgumentException("WEBHOOK 채널은 webhookUrl이 필수입니다")
        }

        val subscription = Subscription(
            userId = request.userId,
            eventType = request.eventType,
            channel = request.channel,
            webhookUrl = request.webhookUrl
        )
        val saved = subscriptionRepository.save(subscription)
        subscriptionCacheService.refreshCache(request.eventType)
        return saved
    }

    fun getSubscriptions(userId: String): List<Subscription> {
        return subscriptionRepository.findByUserId(userId)
    }

    fun updateSubscription(userId: String, eventType: String, channel: Channel, request: SubscriptionUpdateRequest): Subscription {
        val subscription = subscriptionRepository.findByUserIdAndEventTypeAndChannel(userId, eventType, channel)
            ?: throw ResourceNotFoundException("구독을 찾을 수 없습니다")

        subscription.status = request.status
        subscription.updatedAt = java.time.LocalDateTime.now()
        val saved = subscriptionRepository.save(subscription)
        subscriptionCacheService.refreshCache(eventType)
        return saved
    }
}
