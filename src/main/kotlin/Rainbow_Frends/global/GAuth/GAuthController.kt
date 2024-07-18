package Rainbow_Frends.global.GAuth

import gauth.GAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "GAuth", description = "GAuth API")
@RestController
class GAuthUserController(private val gauthRepository: GAuthRepository, private val gAuth: GAuth) {

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
    fun redirect(@RequestParam code: String): String {
        GAuthService(gAuth).fetchAccessToken(code)
        return "Sfsf"
    }

    @Operation(summary = "GAuthUserEntity 값 확인", description = "현재 인증된 사용자의 GAuthUserEntity값 확인")
    @GetMapping("/user/me")
    fun userMe(): Rainbow_Frends.global.GAuth.GAuth? {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        val email: String = auth.name
        return gauthRepository.findByEmail(email).orElseThrow { RuntimeException("entity를 찾을 수 없습니다.") }
    }
}