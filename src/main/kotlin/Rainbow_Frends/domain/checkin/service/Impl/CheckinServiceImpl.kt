package Rainbow_Frends.domain.checkin.service.Impl

import Rainbow_Frends.domain.User.repository.UserRepository
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.checkin.entity.Checkin
import Rainbow_Frends.domain.checkin.repository.CheckinRepository
import Rainbow_Frends.domain.checkin.service.CheckinService
import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class CheckinServiceImpl(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository,
    private val checkinRepository: CheckinRepository
) : CheckinService {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    @PostConstruct
    override fun initializeKeys() {
        createKeysForTodayAndTomorrow()
    }

    @Scheduled(cron = "0 30 21 * * *")
    fun scheduledKeyCreation() {
        createKeysForTodayAndTomorrow()
        incrementLateNumberForUncheckedStudents()
        deleteOldCheckins()
    }

    override fun createCheckinKeyForDate(date: LocalDate): Boolean {
        val users = userRepository.findAll()
        val checkinsForDate = users.map { user ->
            Checkin(
                user = user,
                checkintruefalse = false,
                checkinDate = date
            )
        }
        checkinRepository.saveAll(checkinsForDate)
        return true
    }

    override fun addNewUserToCheckin(userId: UUID) {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val checkins = mutableListOf<Checkin>()

        if (checkinRepository.findByUserAndCheckinDate(user, today).isEmpty()) {
            checkins.add(Checkin(user = user, checkintruefalse = false, checkinDate = today))
        }
        if (checkinRepository.findByUserAndCheckinDate(user, tomorrow).isEmpty()) {
            checkins.add(Checkin(user = user, checkintruefalse = false, checkinDate = tomorrow))
        }

        checkinRepository.saveAll(checkins)
    }

    private fun createKeysForTodayAndTomorrow() {
        val today = LocalDate.now()
        if (today.dayOfWeek == DayOfWeek.FRIDAY) {
            return
        }
        createCheckinKeyForDate(today)
        createCheckinKeyForDate(today.plusDays(1))
    }

    private fun incrementLateNumberForUncheckedStudents() {
        val today = LocalDate.now()
        val uncheckedCheckins = checkinRepository.findByCheckintruefalseAndCheckinDate(false, today)
        uncheckedCheckins.forEach { checkin ->
            val account = accountRepository.findByStudentId(checkin.user.studentNum!!.generateStudentId())
            account?.let {
                it.lateNumber = (it.lateNumber ?: 0) + 1
                accountRepository.save(it)
            }
        }
    }

    private fun deleteOldCheckins() {
        val twoDaysAgo = LocalDate.now().minusDays(2)
        val oldCheckins = checkinRepository.findByCheckinDateBefore(twoDaysAgo)
        checkinRepository.deleteAll(oldCheckins)
    }
}
