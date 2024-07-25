package Rainbow_Frends.domain.times.controller

import Rainbow_Frends.domain.times.TimeResponse
import Rainbow_Frends.global.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.time.LocalDateTime

@Tag(name = "시간", description = "시간 조회API")
@RestController
@RequestMapping("/times")
class TimeController {
    @Operation(summary = "입실 남은 시간 조회", description = "입실 마감까지 남은 시간 조회 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/remaintime")
    fun remainTime(): Response<TimeResponse> {
        val nowTime = LocalDateTime.now()
        println("test:: $nowTime")
        val limitTime = LocalDateTime.of(nowTime.year, nowTime.month, nowTime.dayOfMonth, 21, 30, 0)
        val adjustedLimitTime = if (nowTime.isAfter(limitTime)) limitTime.plusDays(1) else limitTime
        val duration = Duration.between(nowTime, adjustedLimitTime)
        val hoursUntilLimit = duration.toHours()
        val minutesUntilLimit = duration.toMinutes() % 60
        val secondsUntilLimit = duration.seconds % 60
        val remaining = TimeResponse(hoursUntilLimit, minutesUntilLimit, secondsUntilLimit)
        return Response("성공", "남은 시간 조회 성공", remaining)
    }
}