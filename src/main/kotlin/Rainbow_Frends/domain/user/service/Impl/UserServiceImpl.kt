package Rainbow_Frends.domain.user.service.Impl

import Rainbow_Frends.domain.user.entity.User
import Rainbow_Frends.domain.user.repository.UserRepository
import Rainbow_Frends.domain.user.service.UserService
import Rainbow_Frends.domain.checkin.service.CheckinService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository, private val checkinService: CheckinService
) : UserService {
    override fun createUser(user: User): User {
        val savedUser = userRepository.save(user)
        savedUser.id?.let {
            checkinService.addNewUserToCheckin(it)
        } ?: throw IllegalStateException("User ID should not be null after saving")
        return savedUser
    }
}