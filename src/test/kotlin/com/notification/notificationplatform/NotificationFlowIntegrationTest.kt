package com.notification.notificationplatform

import com.notification.notificationplatform.dlq.FailedNotificationRepository
import com.notification.notificationplatform.event.Channel
import com.notification.notificationplatform.event.EventService
import com.notification.notificationplatform.notification.NotificationRepository
import com.notification.notificationplatform.notification.NotificationStatus
import com.notification.notificationplatform.subscription.Subscription
import com.notification.notificationplatform.subscription.SubscriptionRepository
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.kotlin.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import com.notification.notificationplatform.config.TestContainersConfig
import java.time.Duration

@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig::class)
class NotificationFlowIntegrationTest {

    @Autowired lateinit var eventService: EventService
    @Autowired lateinit var subscriptionRepository: SubscriptionRepository
    @Autowired lateinit var notificationRepository: NotificationRepository
    @Autowired lateinit var failedNotificationRepository: FailedNotificationRepository

    @BeforeEach
    fun setup() {
        failedNotificationRepository.deleteAll()
        notificationRepository.deleteAll()
        subscriptionRepository.deleteAll()
    }

    @Test
    fun `이벤트 생성 시 구독자에게 알림이 발송된다`() {
        // given
        subscriptionRepository.save(Subscription(
            userId = "user-1",
            eventType = "BUILD_FAILED",
            channel = Channel.SLACK
        ))

        // when
        val result = eventService.createEvent(
            idempotencyKey = "test-001",
            source = "jenkins",
            eventType = "BUILD_FAILED",
            payload = "PR #1 빌드 실패"
        )

        // then
        assertThat(result.isDuplicate).isFalse()

        await.atMost(Duration.ofSeconds(5)).untilAsserted {
            val notifications = notificationRepository.findAll()
            assertThat(notifications).hasSize(1)
            assertThat(notifications[0].status).isEqualTo(NotificationStatus.SENT)
            assertThat(notifications[0].userId).isEqualTo("user-1")
        }
    }

    @Test
    fun `멱등성 키가 같으면 알림이 중복 생성되지 않는다`() {
        // given
        subscriptionRepository.save(Subscription(
            userId = "user-1",
            eventType = "BUILD_FAILED",
            channel = Channel.SLACK
        ))

        // when
        eventService.createEvent("dup-001", "jenkins", "BUILD_FAILED", "첫 요청")
        val second = eventService.createEvent("dup-001", "jenkins", "BUILD_FAILED", "중복 요청")

        // then
        assertThat(second.isDuplicate).isTrue()

        await.atMost(Duration.ofSeconds(5)).untilAsserted {
            val notifications = notificationRepository.findAll()
            assertThat(notifications).hasSize(1)
        }
    }

    @Test
    fun `발송 실패 시 재시도 후 DLQ에 저장된다`() {
        // given
        subscriptionRepository.save(Subscription(
            userId = "user-fail",
            eventType = "DEPLOY_FAILED",
            channel = Channel.SLACK
        ))

        // when
        eventService.createEvent(
            idempotencyKey = "fail-001",
            source = "jenkins",
            eventType = "DEPLOY_FAILED",
            payload = "[SIMULATE_FAILURE] 배포 실패"
        )

        // then
        await.atMost(Duration.ofSeconds(15)).untilAsserted {
            val notifications = notificationRepository.findAll()
            assertThat(notifications).hasSize(1)
            assertThat(notifications[0].status).isEqualTo(NotificationStatus.FAILED)
            assertThat(notifications[0].retryCount).isGreaterThan(0)

            val dlq = failedNotificationRepository.findAll()
            assertThat(dlq).hasSize(1)
            assertThat(dlq[0].channel).isEqualTo(Channel.SLACK)
        }
    }

    @Test
    fun `구독자가 없으면 알림이 생성되지 않는다`() {
        // when
        val result = eventService.createEvent(
            idempotencyKey = "no-sub-001",
            source = "monitoring",
            eventType = "CPU_ALERT",
            payload = "CPU 90%"
        )

        // then
        assertThat(result.isDuplicate).isFalse()

        await.atMost(Duration.ofSeconds(3)).untilAsserted {
            assertThat(notificationRepository.findAll()).isEmpty()
        }
    }
}
