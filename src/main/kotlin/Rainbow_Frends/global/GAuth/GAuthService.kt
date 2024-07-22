package Rainbow_Frends.global.GAuth

import Rainbow_Frends.global.GAuth.JWT.JWTUtil
import Rainbow_Frends.global.User.UserService
import gauth.GAuthUserInfo
import org.springframework.stereotype.Service

@Service
class GauthService(
    private val jwtUtil: JWTUtil,
    private val userService: UserService
) {

    fun loginWithGauth(accessCode: String): GAuthUserInfo {
        // GAuth 로그인 로직
        // 여기는 placeholder입니다. 실제 GAuth 로그인 로직으로 대체하세요.
        return userService.findByAccessCode(accessCode)
    }

    fun createToken(user: GAuthUserInfo): String {
        return jwtUtil.createJwt(user.name, user.id, 86400000L)
    }
}