package Rainbow_Frends.domain.account.repository

import Rainbow_Frends.domain.account.entity.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository: JpaRepository<Account, Long> {
}