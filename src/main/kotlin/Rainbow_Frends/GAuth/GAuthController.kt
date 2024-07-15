package Rainbow_Frends.GAuth

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class controlloer {
    @GetMapping("/role/student")
    fun student(): String {
        return "hi student!"
    }

    @GetMapping("/role/teacher")
    fun teacher(): String {
        return "hi teacher!"
    }

    @GetMapping("/auth/me")
    fun me(): Any {
        val auth = SecurityContextHolder.getContext().authentication
        return auth.principal
    }

    @GetMapping("/page")
    fun redirect(): String {
        return "test"
    }
}