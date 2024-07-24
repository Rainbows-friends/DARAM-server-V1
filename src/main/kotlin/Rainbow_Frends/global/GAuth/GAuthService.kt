package Rainbow_Frends.global.GAuth

import Rainbow_Frends.global.GAuth.JWT.JWTUtil
import Rainbow_Frends.global.GAuth.user.service.UserService
import gauth.GAuthUserInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject

@Service
class GAuthService(
    private val jwtUtil: JWTUtil, @Lazy private val userService: UserService, private val restTemplate: RestTemplate
) {

    @Value("\${GAuth-CLIENT-ID}")
    private val clientId: String? = null

    @Value("\${GAuth-CLIENT-SECRET}")
    private val clientSecret: String? = null

    @Value("\${GAuth-REDIRECT-URI}")
    private val redirectUrl: String? = null

    fun getAccessToken(code: String): String {
        val tokenUrl = "https://gauth.com/token"
        val request = mapOf(
            "client_id" to clientId,
            "client_secret" to clientSecret,
            "redirect_uri" to redirectUrl,
            "code" to code,
            "grant_type" to "authorization_code"
        )
        val response: Map<String, String> = restTemplate.postForObject(tokenUrl, request)
        return response["access_token"] ?: throw IllegalStateException("Access token not found")
    }

    fun getUserInfoByCode(accessToken: String): GAuthUserInfo {
        val userInfoUrl = "https://gauth.com/userinfo"
        val headers = org.springframework.http.HttpHeaders()
        headers.set("Authorization", "Bearer $accessToken")
        val entity = HttpEntity<String>(headers)
        val response: ResponseEntity<GAuthUserInfo> =
            restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, GAuthUserInfo::class.java)
        return response.body ?: throw IllegalStateException("User info not found")
    }

    fun createToken(user: GAuthUserInfo): String {
        val student_id: Long = (user.grade + user.classNum + if (user.num < 10) 0 + user.num else user.num).toLong()
        return jwtUtil.createJwt(user.name, student_id, 86400000L)
    }

    fun loginWithGauth(accessCode: String): GAuthUserInfo {
        return getUserInfoByCode(accessCode)
    }
}