package com.notification.notificationplatform.event

import com.notification.notificationplatform.notification.Notification
import com.notification.notificationplatform.notification.NotificationRepository
import com.notification.notificationplatform.sender.NotificationCreatedEvent
import com.notification.notificationplatform.subscription.SubscriptionCacheService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper

@Service
class EventService (
    private val eventRepository: EventRepository,
    private val subscriptionCacheService: SubscriptionCacheService,
    private val notificationRepository: NotificationRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper
){
    fun getEventByIdempotencyKey(idempotencyKey: String): Event? {
        return eventRepository.findByIdempotencyKey(idempotencyKey)
    }

    @Transactional
    fun createEvent(
        idempotencyKey: String,
        source: String,
        eventType: String,
        payload: String?
    ): EventCreateResult {
        val exist = getEventByIdempotencyKey(idempotencyKey)
        if (exist != null)
            return EventCreateResult(exist, 0, isDuplicate = true)

        val event = Event(
            idempotencyKey = idempotencyKey,
            source = source,
            eventType = eventType,
            payload = payload
        )

        eventRepository.save(event)

        val subscriptions = subscriptionCacheService.findActiveSubscriptions(eventType)
        val notifications = subscriptions.map { subscription ->
            Notification(
                eventId = event.id!!,
                userId = subscription.userId,
                channel = subscription.channel,
                message = event.payload ?: eventType,
                metadata = if (subscription.channel == Channel.WEBHOOK)
                    objectMapper.writeValueAsString(mapOf("webhookUrl" to subscription.webhookUrl))
                else null
            )
        }

        val savedNotifications = notificationRepository.saveAll(notifications)

        val notificationIds = savedNotifications.map { it.id!! }
        if (notificationIds.isNotEmpty()) {
            eventPublisher.publishEvent(NotificationCreatedEvent(notificationIds))
        }

        return EventCreateResult(event, savedNotifications.size)
    }
}