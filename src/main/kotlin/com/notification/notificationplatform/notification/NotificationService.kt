package com.notification.notificationplatform.notification

import com.notification.notificationplatform.event.Channel
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class NotificationService(private val notificationRepository: NotificationRepository) {

    fun getNotifications(
        userId: String,
        channel: Channel?,
        status: NotificationStatus?,
        cursor: Long?,
        pageSize: Int
    ): NotificationPageResponse {
        // 1개 더 요청해서 다음 페이지 존재 여부 판단
        val notifications = notificationRepository.findByFilters(
            userId = userId,
            cursor = cursor,
            channel = channel,
            status = status,
            pageable = PageRequest.of(0, pageSize + 1)
        )

        val hasNext = notifications.size > pageSize
        val content = if (hasNext) notifications.dropLast(1) else notifications

        val response = content.map {
            NotificationResponse(
                notificationId = it.id!!,
                eventId = it.eventId,
                channel = it.channel,
                message = it.message,
                status = it.status,
                createdAt = it.createdAt,
                sentAt = it.sentAt
            )
        }

        val nextCursor = if (hasNext) content.last().id else null

        return NotificationPageResponse(
            content = response,
            nextCursor = nextCursor,
            hasNext = hasNext
        )
    }
}
