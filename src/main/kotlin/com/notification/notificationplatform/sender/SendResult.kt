package com.notification.notificationplatform.sender

sealed class SendResult {
    object Success: SendResult()
    data class Failure(val reason: String): SendResult()
}