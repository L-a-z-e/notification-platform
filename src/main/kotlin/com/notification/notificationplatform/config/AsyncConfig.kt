package com.notification.notificationplatform.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
@EnableAsync
class AsyncConfig {
    private val logger = LoggerFactory.getLogger(AsyncConfig::class.java)

    @Bean
    fun taskExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 5
        executor.maxPoolSize = 10
        executor.setQueueCapacity(25)
        executor.setThreadNamePrefix("Notification-")
        executor.setRejectedExecutionHandler { _, _ ->
            logger.error("알림 발송 작업 거부됨 — 스레드 풀 포화")
        }
        executor.initialize()
        return executor
    }
}