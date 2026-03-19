package com.notification.notificationplatform.event

import org.springframework.stereotype.Service

@Service
class EventService (private val eventRepository: EventRepository){
    fun getEventByIdempotencyKey(idempotencyKey: String): Event? {
        return eventRepository.findByIdempotencyKey(idempotencyKey)
    }

    fun getEventsByRecipientAndChannel(recipient: String, channel: Channel): List<Event> {
        return eventRepository.findByRecipientAndChannel(recipient, channel)
    }

    fun createEvent(
        idempotencyKey: String,
        sender: String?,
        channel: Channel,
        recipient: String,
        message: String?
    ): Event {
        val exist = getEventByIdempotencyKey(idempotencyKey)
        if (exist != null)
            return exist

        val event = Event(
            idempotencyKey = idempotencyKey,
            sender = sender,
            channel = channel,
            recipient = recipient,
            message = message
        )

        return eventRepository.save(event)
    }
}