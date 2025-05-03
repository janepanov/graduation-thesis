package mk.ukim.finki.soa.graduationthesis.infrastructure.client

import org.springframework.stereotype.Component

@Component
class NotificationServiceFallback : NotificationServiceClient {
    override fun sendEmail(request: EmailNotificationRequest): Boolean {
        // Log the failed attempt
        return false
    }

    override fun sendSms(request: SmsNotificationRequest): Boolean {
        // Log the failed attempt
        return false
    }
}