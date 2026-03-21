package com.notification.notificationplatform.dlq

import com.notification.notificationplatform.event.Channel
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "failed_notifications")
class FailedNotification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "failed_notification_id")
    val id: Long? = null,

    @Column(name = "notification_id", nullable = false)
    val notificationId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    val channel: Channel,

    @Column(name = "error_message", nullable = false, columnDefinition = "TEXT")
    val errorMessage: String,

    @Column(name = "failed_at", nullable = false)
    val failedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "reprocessed", nullable = false)
    var reprocessed: Boolean = false
)