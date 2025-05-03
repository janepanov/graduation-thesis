package mk.ukim.finki.soa.graduationthesis.infrastructure.client

import org.springframework.stereotype.Component

@Component
class UserServiceFallback : UserServiceClient {
    override fun validateUser(userId: String): Boolean {
        // Fallback implementation when user service is down
        return true // Assume user is valid in fallback
    }

    override fun getUserRole(userId: String): String {
        // Fallback implementation when user service is down
        return "UNKNOWN" // Return unknown role in fallback
    }
}