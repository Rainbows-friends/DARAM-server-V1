package Rainbow_Frends.domain.account.util

import Rainbow_Frends.domain.user.entity.User
import Rainbow_Frends.domain.user.repository.UserRepository
import Rainbow_Frends.domain.account.exception.UserNotFoundException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserUtil(private val userRepository: UserRepository) {
    fun getCurrentUser(): User {
        var email: String = SecurityContextHolder.getContext().authentication.name
        return userRepository.findByEmail(email) ?: throw UserNotFoundException()
    }
}