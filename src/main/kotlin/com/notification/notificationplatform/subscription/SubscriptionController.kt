package com.notification.notificationplatform.subscription

import com.notification.notificationplatform.event.Channel
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/subscriptions")
class SubscriptionController(private val subscriptionService: SubscriptionService) {

    @PostMapping
    fun createSubscription(@RequestBody @Valid request: SubscriptionCreateRequest): ResponseEntity<SubscriptionResponse> {

        val subscription = subscriptionService.createSubscription(request)

        val response = SubscriptionResponse(subscription.id!!, subscription.userId, subscription.eventType, subscription.channel, subscription.webhookUrl, subscription.status, subscription.createdAt)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getSubscriptions(@RequestParam userId: String): ResponseEntity<List<SubscriptionResponse>> {
        val subscriptions = subscriptionService.getSubscriptions(userId)

        val response = subscriptions.map{
            SubscriptionResponse(
                subscriptionId = it.id!!,
                userId = it.userId,
                eventType = it.eventType,
                channel = it.channel,
                webhookUrl = it.webhookUrl,
                status = it.status,
                createdAt = it.createdAt
            )
        }

        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @PatchMapping
    fun updateSubscription(
        @RequestParam userId: String,
        @RequestParam eventType: String,
        @RequestParam channel: Channel,
        @Valid @RequestBody request: SubscriptionUpdateRequest
    ): ResponseEntity<SubscriptionResponse> {
        val subscription = subscriptionService.updateSubscription(userId, eventType, channel, request)

        val response = SubscriptionResponse(
            subscriptionId = subscription.id!!,
            userId = subscription.userId,
            eventType = subscription.eventType,
            channel = subscription.channel,
            webhookUrl = subscription.webhookUrl,
            status = subscription.status,
            createdAt = subscription.createdAt
        )

        return ResponseEntity.ok(response)
    }
}