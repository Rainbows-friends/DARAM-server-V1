package Rainbow_Frends.domain.account.repository.jpa

import Rainbow_Frends.domain.account.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Int> {
    fun findByStudentId(studentId: Int): Account?
}