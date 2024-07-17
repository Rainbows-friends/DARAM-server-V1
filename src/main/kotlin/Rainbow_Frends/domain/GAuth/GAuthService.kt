package Rainbow_Frends.domain.GAuth

import dev.yangsijun.gauth.core.user.GAuthUser
import dev.yangsijun.gauth.userinfo.DefaultGAuthUserService
import dev.yangsijun.gauth.userinfo.GAuthAuthorizationRequest
import dev.yangsijun.gauth.userinfo.GAuthUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam

@Service
class GAuthService @Autowired constructor(
    private val gAuthRepository: GAuthRepository
) : GAuthUserService<GAuthAuthorizationRequest, GAuthUser> {

    private val delegatingService: GAuthUserService<GAuthAuthorizationRequest, GAuthUser> = DefaultGAuthUserService()

    override fun loadUser(userRequest: GAuthAuthorizationRequest): GAuthUser {
        val gauthUser = delegatingService.loadUser(userRequest)
        val email = gauthUser.getAttribute<String>("email")

        email?.let {
            gAuthRepository.findByEmail(it).orElseGet {
                val entity = createEntity(gauthUser)
                gAuthRepository.save(entity)
            }
        }

        return gauthUser
    }

    private fun createEntity(gauthUser: GAuthUser): GAuth {
        val profileUrl = gauthUser.getAttribute<String>("profileUrl")
        val email = gauthUser.getAttribute<String>("email")
        val name = gauthUser.getAttribute<String>("name")
        val gender = gauthUser.getAttribute<String>("gender")
        val grade = gauthUser.getAttribute<Int>("grade")
        val classNum = gauthUser.getAttribute<Int>("classNum")
        val num = gauthUser.getAttribute<Int>("num")
        val role = gauthUser.getAttribute<String>("role")

        return GAuth(
            id = null,
            profileUrl = profileUrl,
            email = email,
            name = name,
            gender = gender,
            grade = grade,
            classNum = classNum,
            num = num,
            role = role
        )
    }

    private fun accessCode(url: String): Int {
        val temp1=url.split("/").toString()
        var temp2=temp1.split("/").toString()
        temp2=temp2.split("/").toString()
        temp2.split("?").toString()
        temp2
        //https://gauth.co.kr/e5ce3a12484e4a43bc9ac3aee94fdae1 <--이거 보고 API 요청 계속 하기
    }
}