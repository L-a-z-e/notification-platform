package com.notification.notificationplatform.common.exception

class DuplicateResourceException(message: String) : RuntimeException(message)

class ResourceNotFoundException(message: String) : RuntimeException(message)