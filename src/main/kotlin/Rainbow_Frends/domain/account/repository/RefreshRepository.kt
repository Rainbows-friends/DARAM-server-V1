package Rainbow_Frends.domain.account.repository

import Rainbow_Frends.domain.account.Token.RefreshToken
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RefreshRepository : CrudRepository<RefreshToken, String> {
    fun findByUserId(memberId: UUID): RefreshToken?
}