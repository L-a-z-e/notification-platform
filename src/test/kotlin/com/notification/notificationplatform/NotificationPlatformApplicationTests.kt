package com.notification.notificationplatform

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import com.notification.notificationplatform.config.TestContainersConfig

@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainersConfig::class)
class NotificationPlatformApplicationTests {

    @Test
    fun contextLoads() {
    }

}
