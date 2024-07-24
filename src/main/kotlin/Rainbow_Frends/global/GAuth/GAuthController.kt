package Rainbow_Frends.global.GAuth

import Rainbow_Frends.global.GAuth.JWT.JWTUtil
import Rainbow_Frends.global.GAuth.user.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GAuthController(
    private val gauthService: GAuthService, private val userService: UserService, private val jwtUtil: JWTUtil
) {

    @Value("\${GAuth-CLIENT-ID}")
    private val clientId: String? = null

    @Value("\${GAuth-REDIRECT-URI}")
    private val redirectUrl: String? = null

    @GetMapping("/gauth/authorization")
    fun redirectToGauth(): ResponseEntity<Void> {
        val url = "https://gauth.com/authorize?client_id=$clientId&redirect_uri=$redirectUrl"
        return ResponseEntity.status(HttpStatus.FOUND).location(java.net.URI(url)).build()
    }

    @GetMapping("/page")
    fun handleGauthRedirect(@RequestParam code: String): ResponseEntity<String> {
        val accessToken = gauthService.getAccessToken(code)
        val userInfo = gauthService.getUserInfoByCode(accessToken)
        val user = userService.getUserByName(userInfo.name)
        val student_id: Long =
            (userInfo.grade + userInfo.classNum + if (userInfo.num < 10) 0 + userInfo.num else userInfo.num).toLong()
        val jwt = jwtUtil.createJwt(userInfo.name, student_id, 86400000L)
        return ResponseEntity.ok(jwt)
    }
}