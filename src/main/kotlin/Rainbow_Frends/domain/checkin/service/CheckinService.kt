package Rainbow_Frends.domain.checkin.service

import java.time.LocalDate
import java.util.*

interface CheckinService {
    fun initializeKeys()
    fun createCheckinKeyForDate(date: LocalDate): Boolean
    fun addNewUserToCheckin(userId: UUID)
}