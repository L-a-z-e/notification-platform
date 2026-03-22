package com.notification.notificationplatform.event

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/events")
class EventController(private val eventService: EventService) {

    @PostMapping
    fun createEvent(
        @RequestHeader("idempotency-key") idempotencyKey: String,
        @Valid @RequestBody request: EventCreateRequest
    ): ResponseEntity<EventCreateResponse> {
        val result = eventService.createEvent(
            idempotencyKey = idempotencyKey,
            source = request.source,
            eventType = request.eventType,
            payload = request.payload
        )

        if (result.isDuplicate) {
            return ResponseEntity.ok(
                EventCreateResponse(status = "ALREADY_PROCESSED")
            )
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
            EventCreateResponse(
                eventId = result.event!!.id,
                status = "ACCEPTED"
            )
        )
    }
}
