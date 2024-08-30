package Rainbow_Frends.domain.notice.controller

import Rainbow_Frends.domain.User.entity.User
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.notice.dto.request.NoticeRequest
import Rainbow_Frends.domain.notice.entity.Notice
import Rainbow_Frends.domain.notice.service.Impl.NoticeServiceImpl
import Rainbow_Frends.global.security.jwt.JwtProvider
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@Tag(name = "공지", description = "공지 관련 API")
@RestController
@RequestMapping("/api/notice")
class NoticeController(
    private val noticeService: NoticeServiceImpl,
    private val jwtProvider: JwtProvider,
    private val accountRepository: AccountRepository
) {

    @Operation(summary = "공지 조회 API", description = "공지의 제목, 글쓴이, 본문, 작성시각 조회 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    fun readAll(): List<Notice> {
        return noticeService.readAllNotice()
    }

    @Operation(summary = "공지 등록 API", description = "공지를 등록하는 API")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun postNotice(@RequestBody noticeRequest: NoticeRequest, request: HttpServletRequest) {
        val accessToken = jwtProvider.resolveToken(request)
        val authentication = jwtProvider.getAuthentication(accessToken!!)
        val userDetails = authentication.principal as UserDetails

        val user = noticeService.getUserByUsername(userDetails.username) // User 객체를 가져오는 로직

        // 비즈니스 로직은 서비스 레이어에서 처리
        noticeService.createNotice(noticeRequest, user)
    }
}
