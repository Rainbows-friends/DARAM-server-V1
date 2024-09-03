package Rainbow_Frends.domain.checkin.controller

import Rainbow_Frends.domain.checkin.entity.Checkin
import Rainbow_Frends.domain.checkin.repository.CheckinRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/checkin")
@Tag(name = "기숙사 입실 관리 API", description = "사용자 입실 정보 API")
class CheckinController(private val checkinRepository: CheckinRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/unchecked")
    fun getUncheckedCheckin(): List<Checkin> {
        val today = LocalDate.now()
        logger.info(today.toString())
        return checkinRepository.findByCheckintruefalseAndCheckinDate(false, today)
    }
}