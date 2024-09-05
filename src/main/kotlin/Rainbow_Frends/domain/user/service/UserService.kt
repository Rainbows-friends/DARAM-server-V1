package Rainbow_Frends.domain.user.service

import Rainbow_Frends.domain.user.entity.User

interface UserService {
    fun createUser(user: User): User
}