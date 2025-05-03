package mk.ukim.finki.soa.graduationthesis.infrastructure.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "notification-service",
    url = "\${services.notification-service.url}",
    fallback = NotificationServiceFallback::class
)
interface NotificationServiceClient {
    @PostMapping("/api/notifications/email")
    fun sendEmail(@RequestBody request: EmailNotificationRequest): Boolean

    @PostMapping("/api/notifications/sms")
    fun sendSms(@RequestBody request: SmsNotificationRequest): Boolean
}

data class EmailNotificationRequest(
    val to: String,
    val subject: String,
    val body: String
)

data class SmsNotificationRequest(
    val to: String,
    val message: String
)