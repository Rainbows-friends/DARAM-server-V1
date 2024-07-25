package Rainbow_Frends.domain.account.service

import Rainbow_Frends.domain.User.entity.Authority
import Rainbow_Frends.domain.User.entity.StudentNum
import Rainbow_Frends.domain.User.entity.User
import Rainbow_Frends.domain.User.repository.UserRepository
import Rainbow_Frends.domain.account.presentation.dto.request.SignInRequest
import Rainbow_Frends.domain.account.presentation.dto.response.TokenResponse
import Rainbow_Frends.global.annotation.ServiceWithTransaction
import Rainbow_Frends.global.security.jwt.JwtProvider
import gauth.GAuth
import gauth.GAuthUserInfo
import gauth.exception.GAuthException
import org.springframework.beans.factory.annotation.Value
import java.util.*

@ServiceWithTransaction
class SignInServiceImpl(
    private val gAuth: GAuth,
    private val refreshRepository: RefreshRepository,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider
) : SignInService() {

    @Value("\${GAuth-CLIENT-ID}")
    private lateinit var clientId: String

    @Value("\${GAuth-CLIENT-SECRET}")
    private lateinit var clientSecret: String

    @Value("\${GAuth-REDIRECT-URI}")
    private lateinit var redirectUri: String

    override fun execute(signInRequest: SignInRequest): TokenResponse {
        try {
            val gAuthToken = gAuth.generateToken(
                signInRequest.code,
                clientId,
                clientSecret,
                redirectUri
            )

            val userInfo = gAuth.getUserInfo(gAuthToken.accessToken)

            val user = userRepository.findByEmail(userInfo.email).orElseGet { saveUser(userInfo) }
                ?: throw UserNotFoundException()

            val tokenResponse = jwtProvider.generateTokenDto(user.id)

            saveRefreshToken(tokenResponse, user)

            return tokenResponse

        } catch (e: GAuthException) {
            throw GAuthException(e.code)
        }
    }

    private fun saveUser(gAuthUserInfo: GAuthUserInfo): User? {
        return when (gAuthUserInfo.role) {
            "ROLE_STUDENT" -> saveStudent(gAuthUserInfo)
            "ROLE_TEACHER" -> saveTeacher(gAuthUserInfo)
            else -> null
        }
    }

    private fun saveStudent(gAuthUserInfo: GAuthUserInfo): User {
        val user = User(
            id = UUID.randomUUID(),
            email = gAuthUserInfo.email,
            name = gAuthUserInfo.name,
            studentNum = StudentNum(gAuthUserInfo.grade, gAuthUserInfo.classNum, gAuthUserInfo.num),
            authority = Authority.ROLE_STUDENT
        )

        userRepository.save(user)
        return user
    }

    private fun saveTeacher(gAuthUserInfo: GAuthUserInfo): User {
        val teacher = User(
            id = UUID.randomUUID(),
            email = gAuthUserInfo.email,
            name = gAuthUserInfo.name,
            studentNum = StudentNum(gAuthUserInfo.grade, gAuthUserInfo.classNum, gAuthUserInfo.num),
            authority = Authority.ROLE_TEACHER
        )

        userRepository.save(teacher)
        return teacher
    }

    private fun saveRefreshToken(tokenResponse: TokenResponse, user: User) {
        val refreshToken = RefreshToken(
            refreshToken = tokenResponse.refreshToken,
            memberId = user.id,
            expiredAt = tokenResponse.refreshTokenExpiresIn
        )

        refreshRepository.save(refreshToken)
    }
}