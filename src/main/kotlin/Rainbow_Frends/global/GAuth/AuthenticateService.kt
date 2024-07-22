package Rainbow_Frends.global.GAuth.auth.service

interface AuthenticateService {
    fun getAccessToken(authorizationCode: String): String
}