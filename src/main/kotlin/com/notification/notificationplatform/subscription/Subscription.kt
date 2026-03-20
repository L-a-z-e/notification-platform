package com.notification.notificationplatform.subscription

import com.notification.notificationplatform.event.Channel
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "subscriptions",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "event_type", "channel"])]
)
class Subscription(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    val id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: String,

    @Column(name = "event_type", nullable = false)
    val eventType: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    val channel: Channel,

    @Column(name = "webhook_url", length = 500)
    val webhookUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: SubscriptionStatus = SubscriptionStatus.ACTIVE,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)