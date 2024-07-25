package Rainbow_Frends.domain.User.repository

import Rainbow_Frends.domain.User.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    fun findbyEmail(email: String): User?
}