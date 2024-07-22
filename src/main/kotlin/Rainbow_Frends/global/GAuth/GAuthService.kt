package Rainbow_Frends.global.GAuth

import Rainbow_Frends.global.GAuth.JWT.JwtAuthenticationFilter
import gauth.GAuth
import gauth.GAuthToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GAuthService(private val gAuth: GAuth) : AuthenticateService {
    private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    @Value("\${GAuth-CLIENT-ID}")
    private val clientId: String? = null

    @Value("\${GAuth-CLIENT-SECRET}")
    private val clientSecret: String? = null

    @Value("\${GAuth-REDIRECT-URI}")
    private val redirectUrl: String? = null

    override fun getAccessToken(authorizationCode: String?): String? {
        if (authorizationCode == null) {
            logger.warn("authorizationCode 오류: 값이 null입니다")
            throw IllegalArgumentException("Authorization code is null")
        }

        val token: GAuthToken = gAuth.generateToken(
            authorizationCode, clientId, clientSecret, redirectUrl
        )
        return token.accessToken
    }

    fun getUserInfoByCode(accessCode: String): Any {
        val accessToken: String? = getAccessToken(accessCode)
        if (accessToken != null) {
            return gAuth.getUserInfo(accessToken)
        } else {
            logger.warn("accessCode 오류: 값이 null입니다")
            throw IllegalArgumentException("Access code is invalid or null")
        }
    }
}