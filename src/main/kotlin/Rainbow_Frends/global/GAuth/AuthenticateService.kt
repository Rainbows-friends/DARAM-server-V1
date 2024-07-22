package Rainbow_Frends.global.GAuth

interface AuthenticateService {
    open fun getAccessToken(authorizationCode: String?): String?
}