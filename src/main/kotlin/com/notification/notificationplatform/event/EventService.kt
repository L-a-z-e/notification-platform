package com.notification.notificationplatform.event

import org.springframework.stereotype.Service

@Service
class EventService (private val eventRepository: EventRepository){
    fun getEventByIdempotencyKey(idempotencyKey: String): Event? {
        return eventRepository.findByIdempotencyKey(idempotencyKey)
    }

    fun createEvent(
        idempotencyKey: String,
        source: String,
        eventType: String,
        payload: String?
    ): Event {
        val exist = getEventByIdempotencyKey(idempotencyKey)
        if (exist != null)
            return exist

        val event = Event(
            idempotencyKey = idempotencyKey,
            source = source,
            eventType = eventType,
            payload = payload
        )

        return eventRepository.save(event)
    }
}