package com.notification.notificationplatform.notification

import com.notification.notificationplatform.event.Channel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notifications")
class NotificationController(private val notificationService: NotificationService) {

    @GetMapping
    fun getNotifications(
        @RequestParam userId: String,
        @RequestParam(required = false) channel: Channel?,
        @RequestParam(required = false) status: NotificationStatus?,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "20") pageSize: Int
    ): ResponseEntity<NotificationPageResponse> {
        val response = notificationService.getNotifications(userId, channel, status, cursor, pageSize)
        return ResponseEntity.ok(response)
    }
}
