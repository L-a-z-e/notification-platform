package com.notification.notificationplatform.notification

import com.notification.notificationplatform.event.Channel
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface NotificationRepository: JpaRepository<Notification, Long> {
    @Query("""
        SELECT n FROM Notification n
        WHERE n.userId = :userId
        AND (:cursor IS NULL OR n.id < :cursor)
        AND (:channel IS NULL OR n.channel = :channel)
        AND (:status IS NULL OR n.status = :status)
        ORDER BY n.id DESC
    """)
    fun findByFilters(
        userId: String,
        cursor: Long?,
        channel: Channel?,
        status: NotificationStatus?,
        pageable: Pageable
    ): List<Notification>
}