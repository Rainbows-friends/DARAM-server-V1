package Rainbow_Frends.global.auth

import Rainbow_Frends.domain.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class GetStudentId(private val userRepository: UserRepository) {
    fun getStudentId(email:String): Int {
        val user = email?.let { userRepository.findByEmail(it) } ?: throw RuntimeException("이메일로 사용자를 찾을 수 없습니다: $email")
        val studentNum = user.studentNum?.let {
            (it.grade * 1000) + (it.classNum * 100) + it.number
        } ?: throw RuntimeException("유효하지 않은 학번 정보입니다.")
        return studentNum
    }
}