package Rainbow_Frends.domain.time.service

import Rainbow_Frends.domain.time.presentation.dto.response.TimesResponse
import Rainbow_Frends.global.log.logger
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime

@Service
class TimeService {
    private val log = logger()
    fun timeUntilLimit(): TimesResponse {
        val nowTime = LocalDateTime.now()
        log.info("Current time: $nowTime")
        val limitTime = LocalDateTime.of(nowTime.year, nowTime.month, nowTime.dayOfMonth, 21, 30, 0)
        val adjustedLimitTime = when (nowTime.dayOfWeek) {
            DayOfWeek.FRIDAY, DayOfWeek.SATURDAY -> LocalDateTime.of(
                nowTime.year, nowTime.month, nowTime.with(DayOfWeek.SUNDAY).dayOfMonth, 21, 30, 0
            )

            else -> if (nowTime.isAfter(limitTime)) limitTime.plusDays(1) else limitTime
        }
        log.info("Limit time set to: $adjustedLimitTime")
        val duration = Duration.between(nowTime, adjustedLimitTime)
        val hoursUntilLimit = duration.toHours()
        val minutesUntilLimit = duration.minusHours(hoursUntilLimit).toMinutes()
        val secondsUntilLimit = duration.minusHours(hoursUntilLimit).minusMinutes(minutesUntilLimit).seconds
        log.info("Time remaining - Hours: $hoursUntilLimit, Minutes: $minutesUntilLimit, Seconds: $secondsUntilLimit")
        return TimesResponse(hoursUntilLimit, minutesUntilLimit, secondsUntilLimit)
    }
}