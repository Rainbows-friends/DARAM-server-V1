package Rainbow_Frends.domain.account.service.impl

import Rainbow_Frends.domain.User.entity.Authority
import Rainbow_Frends.domain.User.entity.StudentNum
import Rainbow_Frends.domain.User.entity.User
import Rainbow_Frends.domain.User.repository.UserRepository
import Rainbow_Frends.domain.account.Token.RefreshToken
import Rainbow_Frends.domain.account.entity.Account
import Rainbow_Frends.domain.account.entity.Role
import Rainbow_Frends.domain.account.exception.UserNotFoundException
import Rainbow_Frends.domain.account.presentation.dto.request.SignInRequest
import Rainbow_Frends.domain.account.presentation.dto.response.TokenResponse
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.account.repository.redis.RefreshRepository
import Rainbow_Frends.domain.account.service.SignInService
import Rainbow_Frends.domain.checkin.service.CheckinService
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
    private val accountRepository: AccountRepository,
    private val jwtProvider: JwtProvider,
    private val checkinService: CheckinService
) : SignInService {

    @Value("\${GAuth-CLIENT-ID}")
    private lateinit var clientId: String

    @Value("\${GAuth-CLIENT-SECRET}")
    private lateinit var clientSecret: String

    @Value("\${GAuth-REDIRECT-URI}")
    private lateinit var redirectUri: String

    override fun execute(signInRequest: SignInRequest): TokenResponse {
        try {
            val gAuthToken = gAuth.generateToken(
                signInRequest.code, clientId, clientSecret, redirectUri
            )
            val userInfo = gAuth.getUserInfo(gAuthToken.accessToken)
            val user = userRepository.findByEmail(userInfo.email)?.let { it } ?: saveUser(userInfo)
            ?: throw UserNotFoundException()
            val tokenResponse = user.id?.let { jwtProvider.generateTokenDto(it) } ?: throw UserNotFoundException()
            saveRefreshToken(tokenResponse, user)
            return tokenResponse
        } catch (e: GAuthException) {
            throw GAuthException(e.code)
        }
    }

    private fun saveUser(gAuthUserInfo: GAuthUserInfo): User? {
        val user = when (gAuthUserInfo.role) {
            "ROLE_STUDENT" -> saveStudent(gAuthUserInfo)
            "ROLE_TEACHER" -> saveTeacher(gAuthUserInfo)
            else -> null
        }
        return user?.let {
            // User 저장 후 UUID를 받아온 후에 Account와 Checkin을 저장합니다.
            saveAccountAndCheckin(it)
            it
        }
    }

    private fun saveStudent(gAuthUserInfo: GAuthUserInfo): User {
        val user = User(
            email = gAuthUserInfo.email,
            name = gAuthUserInfo.name,
            studentNum = StudentNum(gAuthUserInfo.grade, gAuthUserInfo.classNum, gAuthUserInfo.num),
            authority = Authority.ROLE_STUDENT
        )
        return userRepository.save(user) // 저장 후 UUID 생성
    }

    private fun saveTeacher(gAuthUserInfo: GAuthUserInfo): User {
        val teacher = User(
            email = gAuthUserInfo.email,
            name = gAuthUserInfo.name,
            studentNum = StudentNum(gAuthUserInfo.grade, gAuthUserInfo.classNum, gAuthUserInfo.num),
            authority = Authority.ROLE_TEACHER
        )
        return userRepository.save(teacher) // 저장 후 UUID 생성
    }

    private fun saveAccountAndCheckin(user: User) {
        // Account 저장
        val studentNum = user.studentNum ?: throw IllegalArgumentException("StudentNum cannot be null")
        val studentId = (studentNum.grade * 1000) + (studentNum.classNum * 100) + studentNum.number
        val account = Account().apply {
            this.studentId = studentId
            this.role = Role.ROLE_AVERAGE_STUDENT
            this.profilePictureURL = null
            this.profilePictureName = null
            this.lateNumber = 0
            this.floor = null
            this.roomNumber = null
        }
        accountRepository.save(account)

        checkinService.addNewUserToCheckin(user.id!!)
    }

    private fun saveRefreshToken(tokenResponse: TokenResponse, user: User) {
        val refreshToken = user.id?.let {
            RefreshToken(
                refreshToken = tokenResponse.refreshToken, UserId = it, expiredAt = tokenResponse.refreshTokenExpiresIn
            )
        }
        refreshToken?.let { refreshRepository.save(it) }
    }
}
