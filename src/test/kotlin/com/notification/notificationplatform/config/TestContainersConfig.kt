package com.notification.notificationplatform.config

import com.redis.testcontainers.RedisContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer

@TestConfiguration(proxyBeanMethods = false)
class TestContainersConfig {

    @Bean
    @ServiceConnection
    fun postgres(): PostgreSQLContainer<*> {
        return PostgreSQLContainer("postgres:18")
    }

    @Bean
    @ServiceConnection
    fun redis(): RedisContainer {
        return RedisContainer("redis:7-alpine")
    }
}
