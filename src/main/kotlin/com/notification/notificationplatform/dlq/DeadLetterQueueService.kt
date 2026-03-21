package com.notification.notificationplatform.dlq

import com.notification.notificationplatform.notification.Notification
import org.springframework.stereotype.Service

@Service
class DeadLetterQueueService(
    private val failedNotificationRepository: FailedNotificationRepository
) {
    fun sendToDeadLetterQueue(notification: Notification, errorMessage: String) {
        val failedNotification = FailedNotification(
            notificationId = requireNotNull(notification.id) { "DLQ 저장 실패: notification.id가 null" },
            channel = notification.channel,
            errorMessage = errorMessage
        )
        failedNotificationRepository.save(failedNotification)
    }

    fun getUnprocessedFailures(): List<FailedNotification> {
        return failedNotificationRepository.findByReprocessedFalse()
    }

    fun reprocess(failedNotificationId: Long) {
        // TODO: 재처리 로직
    }
}
