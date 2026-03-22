package com.notification.notificationplatform.subscription

import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class SubscriptionCacheService(
    private val subscriptionRepository: SubscriptionRepository
) {

    @Cacheable(value = ["subscriptions"], key = "#eventType")
    fun findActiveSubscriptions(eventType: String): List<CachedSubscription> {
        return subscriptionRepository.findByEventTypeAndStatus(eventType, SubscriptionStatus.ACTIVE)
            .map { CachedSubscription(it.userId, it.channel, it.webhookUrl) }
    }

    @CachePut(value = ["subscriptions"], key = "#eventType")
    fun refreshCache(eventType: String): List<CachedSubscription> {
        return subscriptionRepository.findByEventTypeAndStatus(eventType, SubscriptionStatus.ACTIVE)
            .map { CachedSubscription(it.userId, it.channel, it.webhookUrl) }
    }
}
