package com.notification.notificationplatform.event

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "events")
class Event (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    val id: Long? = null,

    @Column(name = "idempotency_key", nullable = false, unique = true)
    val idempotencyKey: String,

    @Column(name = "sender")
    val sender: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    val channel: Channel,

    @Column(name = "recipient", nullable = false)
    val recipient: String,

    @Column(name = "message", columnDefinition = "TEXT")
    val message: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: EventStatus = EventStatus.QUEUED,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "sent_at")
    var sentAt: LocalDateTime? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()

) {}