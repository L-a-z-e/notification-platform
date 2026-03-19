package com.notification.notificationplatform.event

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/events")
class EventController (private val eventService: EventService){

    @PostMapping
    fun createEvent(@RequestHeader("idempotency-key") idempotencyKey: String, @RequestBody request: EventCreateRequest): ResponseEntity<EventCreateResponse> {
        val event = eventService.createEvent(
            idempotencyKey = idempotencyKey,
            sender = request.sender,
            channel = request.channel,
            recipient = request.recipient,
            message = request.message
        )

        val response = EventCreateResponse(
            eventId = event.id!!,
            status = event.status
        )

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response)
    }
}