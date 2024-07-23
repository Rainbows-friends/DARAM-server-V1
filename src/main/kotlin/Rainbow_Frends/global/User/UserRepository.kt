package Rainbow_Frends.global.GAuth.user.repository

import Rainbow_Frends.global.GAuth.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByName(name: String): Optional<User>
    fun findByGithubId(githubId: Long): Optional<User>
}