package Rainbow_Frends.domain.checkin.controller

import Rainbow_Frends.domain.checkin.entity.Checkin
import Rainbow_Frends.domain.checkin.presentation.dto.request.CheckinUpdateRequest
import Rainbow_Frends.domain.checkin.repository.CheckinRepository
import Rainbow_Frends.domain.user.repository.UserRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/checkin")
@Tag(name = "기숙사 입실 관리 API", description = "사용자 입실 정보 API")
class CheckinController(
    private val checkinRepository: CheckinRepository,
    private val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "미입실 학생 조회 API", description = "당일 21시 30분까지 미입실 상태인 학생의 리스트를 반환하는 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/unchecked")
    fun getUncheckedCheckin(): List<Checkin> {
        val today = LocalDate.now()
        logger.info(today.toString())
        return checkinRepository.findByCheckinStatusAndCheckinDate(false, today)
    }

    @Operation(summary = "입실 학생 조회 API", description = "당일 21시 30분에 입실 상태인 학생의 리스트를 반환하는 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    fun getCheckin(): List<Checkin> {
        val today = LocalDate.now()
        logger.info(today.toString())
        return checkinRepository.findByCheckinStatusAndCheckinDate(true, today)
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "입실 상태 전환 API", description = "원하는 학생의 입실 상태를 변경하는 API")
    @PatchMapping
    fun updateCheckin(@RequestBody request: CheckinUpdateRequest): Checkin {
        val user = userRepository.findByEmail(request.email)
            ?: throw RuntimeException("사용자를 찾을 수 없습니다: ${request.email}")
        val checkin = checkinRepository.findByUser(user)
        checkin[0].checkinStatus = request.checkinStatus
        val updatedCheckin = checkinRepository.save(checkin[0])
        logger.info(updatedCheckin.toString())
        return updatedCheckin
    }
}