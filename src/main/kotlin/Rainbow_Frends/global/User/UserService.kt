package Rainbow_Frends.global.GAuth.user.service

import Rainbow_Frends.global.GAuth.GauthService
import Rainbow_Frends.global.GAuth.JWT.TokenMemoryRepository
import Rainbow_Frends.global.GAuth.user.entity.User
import Rainbow_Frends.global.GAuth.user.repository.UserRepository
import Rainbow_Frends.global.User.UserSignupDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val tokenMemoryRepository: TokenMemoryRepository,
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

    fun registerUser(userSignupDto: UserSignupDto) {
        val user = User(
            name = userSignupDto.name, profileImageUrl = userSignupDto.profileImageUrl
        )
        userRepository.save(user)
    }

    fun loginWithGauth(accessCode: String): User {
        val gauthUser = gauthService.getUserInfoByCode(accessCode)
        return userRepository.findByName(gauthUser.name).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        }
    }
}