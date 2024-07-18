package Rainbow_Frends.global.GAuth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface GAuthRepository : JpaRepository<GAuth, Long> {
    fun findByEmail(email: String): Optional<GAuth>
}