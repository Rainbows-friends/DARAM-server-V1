package Rainbow_Frends.domain.account.repository.redis

import Rainbow_Frends.domain.account.Token.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RefreshRepository : JpaRepository<RefreshToken, String> {
    fun findByUserId(memberId: UUID): RefreshToken?
}