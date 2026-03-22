package com.notification.notificationplatform.config

import com.redis.testcontainers.RedisContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistrar
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName

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

    @Bean
    fun localstack(): LocalStackContainer {
        return LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
            .withServices(LocalStackContainer.Service.SQS)
            .apply {
                start()
                execInContainer(
                    "awslocal", "sqs", "create-queue",
                    "--queue-name", "notification-events",
                    "--region", "ap-northeast-2"
                )
            }
    }

    @Bean
    fun localstackProperties(localstack: LocalStackContainer): DynamicPropertyRegistrar {
        return DynamicPropertyRegistrar { registry ->
            registry.add("spring.cloud.aws.region.static") { localstack.region }
            registry.add("spring.cloud.aws.credentials.access-key") { localstack.accessKey }
            registry.add("spring.cloud.aws.credentials.secret-key") { localstack.secretKey }
            registry.add("spring.cloud.aws.sqs.endpoint") { localstack.getEndpointOverride(LocalStackContainer.Service.SQS).toString() }
        }
    }
}
