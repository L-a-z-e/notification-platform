package com.notification.notificationplatform.event

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventPublisher: ApplicationEventPublisher
) {
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
            return EventCreateResult(exist, isDuplicate = true)

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
