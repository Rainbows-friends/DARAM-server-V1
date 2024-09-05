package Rainbow_Frends.domain.notice.controller

import Rainbow_Frends.domain.account.entity.Account
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.notice.dto.request.NoticeRequest
import Rainbow_Frends.domain.notice.entity.Notice
import Rainbow_Frends.domain.notice.service.Impl.NoticeServiceImpl
import Rainbow_Frends.global.auth.GetStudentId
import Rainbow_Frends.global.auth.GetUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "공지", description = "공지 관련 API")
@RestController
@RequestMapping("/api/notice")
class NoticeController(
    private val noticeService: NoticeServiceImpl,
    private val accountRepository: AccountRepository,
    private val getStudentId: GetStudentId,
    private val getUser: GetUser
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
    fun postNotice(@RequestBody noticeRequest: NoticeRequest, request: HttpServletRequest): ResponseEntity<Notice> {
        val user = noticeService.getUserByUsername(getUser.getUser(request).username)
        return noticeService.createNotice(noticeRequest, user)
    }

    @Operation(summary = "특정 공지 조회 API", description = "특정 공지의 제목, 글쓴이, 본문, 작성시각 조회 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    fun getNotice(@PathVariable id: Long): ResponseEntity<Notice> {
        val notice = noticeService.findNoticeById(id)
        return if (notice != null) {
            ResponseEntity.ok(notice)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "공지 수정 API", description = "공지의 제목, 본문 등을 수정하는 API")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    fun updateNotice(
        @PathVariable id: Long, @RequestBody noticeRequest: NoticeRequest, request: HttpServletRequest
    ): ResponseEntity<Notice> {
        val user = noticeService.getUserByUsername(getUser.getUser(request).username)
        val studentId = getStudentId.getStudentId(user.email!!)
        val account: Account? = studentId.let { accountRepository.findByStudentId(it) }
        val updatedNotice = account?.let { noticeService.updateNotice(id, noticeRequest, it) }
        return if (updatedNotice != null) {
            ResponseEntity.ok(updatedNotice)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "공지 삭제 API", description = "특정 공지를 삭제하는 API")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteNotice(@PathVariable id: Long): ResponseEntity<Void> {
        return if (noticeService.deleteNotice(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}