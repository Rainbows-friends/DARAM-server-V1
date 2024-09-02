package Rainbow_Frends.domain.User.service

import Rainbow_Frends.domain.User.entity.User

interface UserService {
    fun createUser(user: User): User
}