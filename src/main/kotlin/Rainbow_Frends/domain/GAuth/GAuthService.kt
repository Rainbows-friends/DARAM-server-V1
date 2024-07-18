package Rainbow_Frends.domain.GAuth

import gauth.GAuth
import gauth.GAuthToken
import Rainbow_Frends.domain.GAuth.config.GAuthConfig.Component
import org.springframework.stereotype.Service

@Service
class GAuthService(private val gAuth: GAuth) {
    val ClientID = "0906594b6e1a49138b79e5934eaddea575c580063a6844be92a6dcc76050a790"
    val ClientSecret = "39bd0f9270d14cda89a55c54cbffbcb632c4f43b0e184fc1aa7ab5e5c801151d"
    val redirectURI = "https://localhost:5000/page"
    fun fetchAccessToken(code: String): Unit {
        val accass_token:GAuthToken=gAuth.generateToken(code, ClientID, ClientSecret, redirectURI)

        println(accass_token)
    }
}