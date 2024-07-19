package Rainbow_Frends.global.GAuth

import Rainbow_Frends.global.GAuth.JWT.TokenGenerator
import Rainbow_Frends.global.GAuth.JWT.TokenResponse
import gauth.GAuth
import gauth.GAuthToken
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
    fun redirect(@RequestParam code: String): TokenResponse {
        val gAuthToken: GAuthToken = gAuth.generateToken(
            code,
            System.getenv("GAuth-Client-ID"),
            System.getenv("GAuth-Client-Secret"),
            System.getenv("GAuth-Redirect-URI")
        )
        val grade = gAuth.getUserInfo(gAuthToken.accessToken).grade
        val classNum = gAuth.getUserInfo(gAuthToken.accessToken).classNum
        val num = gAuth.getUserInfo(gAuthToken.accessToken).num
        val studentId = grade.toString() + classNum.toString() + num.toString()
        val tokenResponse = TokenGenerator.generateToken(studentId.toLong())
        return tokenResponse
    }

    @Operation(summary = "GAuthUserEntity 값 확인", description = "현재 인증된 사용자의 GAuthUserEntity값 확인")
    @GetMapping("/user/me")
    fun userMe(): Rainbow_Frends.global.GAuth.GAuth? {
        val auth: Authentication = SecurityContextHolder.getContext().authentication
        val email: String = auth.name
        return gauthRepository.findByEmail(email).orElseThrow { RuntimeException("entity를 찾을 수 없습니다.") }
    }
}