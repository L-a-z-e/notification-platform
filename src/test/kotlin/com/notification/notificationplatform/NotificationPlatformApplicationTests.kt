package com.notification.notificationplatform

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import com.notification.notificationplatform.config.TestContainersConfig

@Tag("integration")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig::class)
class NotificationPlatformApplicationTests {

    @Test
    fun contextLoads() {
    }

}
