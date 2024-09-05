package Rainbow_Frends.domain.checkin.repository

import Rainbow_Frends.domain.user.entity.User
import Rainbow_Frends.domain.checkin.entity.Checkin
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface CheckinRepository : JpaRepository<Checkin, Long> {
    fun findByUserAndCheckinDate(user: User, checkinDate: LocalDate): List<Checkin>
    fun findByCheckinStatusAndCheckinDate(checkintruefalse: Boolean, checkinDate: LocalDate): List<Checkin>
    fun findByCheckinDateBefore(checkinDate: LocalDate): List<Checkin>
    fun findByUser(user: User): List<Checkin>
}