package com.notification.notificationplatform.sender

import com.notification.notificationplatform.dlq.DeadLetterQueueService
import com.notification.notificationplatform.notification.NotificationRepository
import com.notification.notificationplatform.notification.NotificationStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class NotificationProcessingService(
    private val notificationRepository: NotificationRepository,
    private val deadLetterQueueService: DeadLetterQueueService,
) {

    @Transactional
    fun updateSuccess(notificationId: Long) {
        val notification = notificationRepository.findById(notificationId)
            .orElseThrow { IllegalStateException("Notification not found: $notificationId") }
        notification.status = NotificationStatus.SENT
        notification.sentAt = LocalDateTime.now()
        notification.updatedAt = LocalDateTime.now()
        notificationRepository.save(notification)
    }

    @Transactional
    fun updateFailure(notificationId: Long, errorMessage: String, retryCount: Int = 0) {
        val notification = notificationRepository.findById(notificationId)
            .orElseThrow { IllegalStateException("Notification not found: $notificationId") }
        notification.status = NotificationStatus.FAILED
        notification.errorMessage = errorMessage
        notification.retryCount = retryCount
        notification.updatedAt = LocalDateTime.now()
        notificationRepository.save(notification)
        deadLetterQueueService.sendToDeadLetterQueue(notification, errorMessage)
    }
}
