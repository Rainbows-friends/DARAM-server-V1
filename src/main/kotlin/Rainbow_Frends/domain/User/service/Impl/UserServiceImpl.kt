package Rainbow_Frends.domain.User.service.Impl

import Rainbow_Frends.domain.User.entity.User
import Rainbow_Frends.domain.User.repository.UserRepository
import Rainbow_Frends.domain.User.service.UserService
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