package com.notification.notificationplatform.event

import org.springframework.data.jpa.repository.JpaRepository

interface EventRepository : JpaRepository<Event, Long> {
    fun findByIdempotencyKey(idempotencyKey: String): Event?
}