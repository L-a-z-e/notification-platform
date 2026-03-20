package com.notification.notificationplatform.subscription

import com.notification.notificationplatform.event.Channel
import org.springframework.data.jpa.repository.JpaRepository

interface SubscriptionRepository : JpaRepository<Subscription, Long> {
    fun findByUserId(userId: String): List<Subscription>
    fun findByUserIdAndEventTypeAndChannel(userId: String, eventType: String, channel: Channel): Subscription?
    fun findByEventTypeAndStatus(eventType: String, status: SubscriptionStatus): List<Subscription>
}
