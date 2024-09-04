package Rainbow_Frends.domain.notice.service.Impl

import Rainbow_Frends.domain.User.entity.User
import Rainbow_Frends.domain.User.repository.UserRepository
import Rainbow_Frends.domain.account.entity.Account
import Rainbow_Frends.domain.account.entity.Role
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.notice.dto.request.NoticeRequest
import Rainbow_Frends.domain.notice.entity.Notice
import Rainbow_Frends.domain.notice.repository.NoticeRepository
import Rainbow_Frends.domain.notice.service.NoticeService
import Rainbow_Frends.global.annotation.ServiceWithTransaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


@ServiceWithTransaction
class NoticeServiceImpl(
    private val noticeRepository: NoticeRepository,
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository
) : NoticeService {

    override fun readAllNotice(): List<Notice> {
        return noticeRepository.findAll()
    }

    fun createNotice(noticeRequest: NoticeRequest, user: User): ResponseEntity<Notice> {
        val studentId = generateStudentId(user)
        val account = accountRepository.findByStudentId(studentId)
            ?: throw RuntimeException("student_id: $studentId 에 해당하는 Account를 찾을 수 없습니다.")
        val notice = Notice(
            title = noticeRequest.title, content = noticeRequest.content, writer = account
        )
        val savedNotice = noticeRepository.save(notice)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotice)
    }

    private fun generateStudentId(user: User): Int {
        val studentNum = user.studentNum ?: throw IllegalArgumentException("User의 studentNum이 null입니다.")
        return studentNum.grade * 1000 + studentNum.classNum * 100 + studentNum.number
    }

    fun getUserByUsername(username: String): User {
        return userRepository.findByEmail(username)
            ?: throw RuntimeException("username: $username 에 해당하는 User를 찾을 수 없습니다.")
    }

    override fun findNoticeById(id: Long): Notice? {
        return noticeRepository.findById(id).orElse(null)
    }

    override fun deleteNotice(id: Long): Boolean {
        val notice = noticeRepository.findById(id).orElse(null) ?: return false
        noticeRepository.delete(notice)
        return true
    }

    override fun updateNotice(id: Long, noticeRequest: NoticeRequest, account: Account): Notice? {
        val notice = noticeRepository.findById(id).orElse(null) ?: return null
        if (account.role == Role.ROLE_DEV || account.role == Role.ROLE_HOUSEFATHER || account.role == Role.ROLE_DORMITORY_MANAGER) {
            notice.title = noticeRequest.title
            notice.content = noticeRequest.content
            return noticeRepository.save(notice)
        }
        return null
    }
}