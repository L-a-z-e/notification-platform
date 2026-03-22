package com.notification.notificationplatform.sender

import com.notification.notificationplatform.notification.Notification
import com.notification.notificationplatform.notification.NotificationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationChunkService(
    private val notificationRepository: NotificationRepository
) {
    private val logger = LoggerFactory.getLogger(NotificationChunkService::class.java)

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveChunk(notifications: List<Notification>): List<Notification> {
        return notificationRepository.saveAll(notifications)
    }
}
