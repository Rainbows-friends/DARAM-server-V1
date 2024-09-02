package Rainbow_Frends.domain.time.controller

import Rainbow_Frends.domain.time.presentation.dto.response.JsonTimesResponse
import Rainbow_Frends.domain.time.presentation.dto.response.TimesResponse
import Rainbow_Frends.domain.time.service.TimeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "시간", description = "시간 조회API")
@RestController
@RequestMapping("/api/times")
class TimeController(private val timeService: TimeService) {
    @Operation(summary = "입실 남은 시간 조회", description = "입실 마감까지 남은 시간 조회 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/remaintime")
    fun remainTime(): JsonTimesResponse<TimesResponse> {
        try {
            val remaining = timeService.timeUntilLimit()
            return JsonTimesResponse("성공", "남은 시간 조회 성공", remaining)
        }catch (e:Exception){
            return JsonTimesResponse("처리 오류","남은 시간 조회 실패",null)
        }
    }
}