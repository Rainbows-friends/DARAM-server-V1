package Rainbow_Frends.domain.account.service.impl

import Rainbow_Frends.domain.User.entity.User
import Rainbow_Frends.domain.account.Token.RefreshToken
import Rainbow_Frends.domain.account.exception.ExpiredRefreshTokenException
import Rainbow_Frends.domain.account.repository.RefreshRepository
import Rainbow_Frends.domain.account.service.LogoutService
import Rainbow_Frends.domain.account.util.UserUtil
import Rainbow_Frends.global.annotation.ServiceWithTransaction
import Rainbow_Frends.global.redis.RedisConfig
import Rainbow_Frends.global.security.jwt.JwtProvider

@ServiceWithTransaction
class LogoutServiceImpl(
    private val refreshRepository: RefreshRepository,
    private val userUtil: UserUtil,
    private val redisUtil: RedisConfig.RedisUtil,
    private val jwtProvider: JwtProvider
) : LogoutService {

    override fun execute(accessToken: String) {
        val user: User = userUtil.getCurrentUser()
        val validRefreshToken: RefreshToken = user.id?.let { userId ->
            refreshRepository.findByUserId(userId)
        } ?: throw ExpiredRefreshTokenException()

        refreshRepository.deleteById(validRefreshToken.refreshToken)
        redisUtil.setBlackList(accessToken, "access_Token", jwtProvider.getExpiration(accessToken))
    }
}