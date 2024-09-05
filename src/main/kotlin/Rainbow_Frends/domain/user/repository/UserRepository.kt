package Rainbow_Frends.domain.user.repository

import Rainbow_Frends.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
    //fun findByName(username: String): User?
}