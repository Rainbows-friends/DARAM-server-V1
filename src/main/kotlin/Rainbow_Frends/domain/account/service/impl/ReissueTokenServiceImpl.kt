package Rainbow_Frends.domain.account.service.impl

import Rainbow_Frends.domain.user.repository.UserRepository
import Rainbow_Frends.domain.account.Token.RefreshToken
import Rainbow_Frends.domain.account.exception.ExpiredRefreshTokenException
import Rainbow_Frends.domain.account.exception.UserNotFoundException
import Rainbow_Frends.domain.account.presentation.dto.response.TokenResponse
import Rainbow_Frends.domain.account.service.ReissueTokenService
import Rainbow_Frends.domain.account.repository.redis.RefreshRepository
import Rainbow_Frends.global.annotation.ServiceWithTransaction
import Rainbow_Frends.global.security.jwt.JwtProvider
import java.time.LocalDateTime
import java.util.*

@ServiceWithTransaction
class ReissueTokenServiceImpl(
    private val jwtProvider: JwtProvider,
    private val refreshRepository: RefreshRepository,
    private val userRepository: UserRepository
) : ReissueTokenService {

    override fun execute(refreshToken: String): TokenResponse {
        val parseRefreshToken = jwtProvider.parseRefreshToken(refreshToken)

        val refreshEntity = parseRefreshToken?.let {
            refreshRepository.findById(it).orElseThrow { ExpiredRefreshTokenException() }
        }

        val user = userRepository.findById(refreshEntity?.UserId!!).orElseThrow { UserNotFoundException() }

        val tokenResponse = jwtProvider.generateTokenDto(user.id!!)

        saveRefreshToken(tokenResponse.refreshToken, user.id, tokenResponse.refreshTokenExpiresIn)

        return tokenResponse
    }

    private fun saveRefreshToken(refreshToken: String, memberId: UUID, expiredAt: LocalDateTime) {
        val token = RefreshToken(
            refreshToken = refreshToken, UserId = memberId, expiredAt = expiredAt
        )
        refreshRepository.save(token)
    }
}