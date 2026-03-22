package com.notification.notificationplatform.event

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val idempotencyKeyRepository: IdempotencyKeyRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun createEvent(
        idempotencyKey: String,
        source: String,
        eventType: String,
        payload: String?
    ): EventCreateResult {
        val isNew = idempotencyKeyRepository.saveIfAbsent(idempotencyKey, Duration.ofHours(24))
        if (!isNew)
            return EventCreateResult(isDuplicate = true)

        val event = Event(
            idempotencyKey = idempotencyKey,
            source = source,
            eventType = eventType,
            payload = payload
        )

        eventRepository.save(event)

        eventPublisher.publishEvent(
            EventAcceptedEvent(
                eventId = event.id!!,
                eventType = event.eventType,
                payload = event.payload
            )
        )

        return EventCreateResult(event)
    }
}
