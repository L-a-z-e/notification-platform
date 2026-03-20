package com.notification.notificationplatform.event

import jakarta.persistence.*
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

    @Column(name = "source", nullable = false)
    val source: String,

    @Column(name = "payload", columnDefinition = "TEXT")
    val payload: String?,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "event_type", nullable = false)
    val eventType: String
) {}