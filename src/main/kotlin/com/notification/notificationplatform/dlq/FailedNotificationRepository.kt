package com.notification.notificationplatform.dlq

import org.springframework.data.jpa.repository.JpaRepository

interface FailedNotificationRepository : JpaRepository<FailedNotification, Long> {
    fun findByReprocessedFalse(): List<FailedNotification>
}
