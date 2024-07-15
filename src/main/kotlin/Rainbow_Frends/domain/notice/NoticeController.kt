package Rainbow_Frends.domain.notice

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "공지", description = "공지 관련 API")
@RestController
@RequestMapping("/notice")
class NoticeController {
    @Operation(summary = "공지 조회 API", description = "공지의 제목,글쓴이,본문 조회 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    fun read(){

    }
}