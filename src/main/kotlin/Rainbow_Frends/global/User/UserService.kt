package Rainbow_Frends.global.User

import Rainbow_Frends.global.GAuth.GAuthService
import Rainbow_Frends.global.GAuth.GAuthUserInfo
import Rainbow_Frends.global.GAuth.GauthService
import Rainbow_Frends.global.GAuth.github.service.GithubService
import Rainbow_Frends.global.GAuth.user.dto.UserSignupDto
import Rainbow_Frends.global.GAuth.user.entity.User
import Rainbow_Frends.global.GAuth.user.repository.TokenMemoryRepository
import Rainbow_Frends.global.GAuth.user.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tokenMemoryRepository: TokenMemoryRepository,
    private val githubService: GithubService,
    private val gauthService: GauthService
) {

    fun getUserById(id: Long): User {
        return userRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        }
    }

    fun getUserByName(name: String): User {
        return userRepository.findByName(name).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        }
    }

    fun getUserByGithubId(githubId: Long): User {
        return userRepository.findByGithubId(githubId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        }
    }

    fun registerUser(userSignupDto: UserSignupDto) {
        val user = User.builder()
            .name(userSignupDto.name)
            .profileImageUrl(userSignupDto.profileImageUrl)
            .build()
        userRepository.save(user)
    }

    fun loginWithGithub(accessCode: String): User {
        val token = githubService.getAccessToken(accessCode)
        val githubUser = githubService.getGithubUserByToken(token)
        val user = userRepository.findByGithubId(githubUser.id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        }
        saveGithubToken(user, token)
        return user
    }

    fun loginWithGauth(accessCode: String): User {
        val gauthUser = gauthService.getUserInfoByCode(accessCode)
        return userRepository.findByName(gauthUser.name).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        }
    }

    private fun saveGithubToken(user: User, githubToken: String) {
        tokenMemoryRepository.saveGithubToken(user, githubToken)
    }
}