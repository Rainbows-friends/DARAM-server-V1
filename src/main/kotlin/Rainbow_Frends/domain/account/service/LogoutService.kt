package Rainbow_Frends.domain.account.service

interface LogoutService {
    fun execute(accessToken: String)
}