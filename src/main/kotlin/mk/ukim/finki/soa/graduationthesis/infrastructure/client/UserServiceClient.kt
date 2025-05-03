package mk.ukim.finki.soa.graduationthesis.infrastructure.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "user-service",
    url = "\${services.user-service.url}",
    fallback = UserServiceFallback::class
)
interface UserServiceClient {
    @GetMapping("/api/users/{userId}/validate")
    fun validateUser(@PathVariable userId: String): Boolean

    @GetMapping("/api/users/{userId}/role")
    fun getUserRole(@PathVariable userId: String): String
}