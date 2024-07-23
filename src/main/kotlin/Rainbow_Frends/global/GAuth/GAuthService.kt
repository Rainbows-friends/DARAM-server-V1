package Rainbow_Frends.global.GAuth

import Rainbow_Frends.global.GAuth.JWT.JWTUtil
import Rainbow_Frends.global.GAuth.user.service.UserService
import gauth.GAuthUserInfo
import org.springframework.stereotype.Service

@Service
class GauthService(
    private val jwtUtil: JWTUtil, private val userService: UserService
) {

    fun getUserInfoByCode(accessCode: String): GAuthUserInfo {
        // 여기에 실제 GAuth 로그인 로직을 추가하세요
        // placeholder 코드를 실제 로직으로 대체해야 합니다
        return userService.findByAccessCode(accessCode)
    }

    fun createToken(user: GAuthUserInfo): String {
        return jwtUtil.createJwt(user.name, user.id, 86400000L)
    }
}