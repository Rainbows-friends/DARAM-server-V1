package Rainbow_Frends.global.auth

import Rainbow_Frends.domain.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class GetStudentId(private val userRepository: UserRepository) {
    fun getStudentId(email: String): Int {
        val user =
            email.let { userRepository.findByEmail(it) } ?: throw RuntimeException("User not found by email: $email")
        val studentNum = user.studentNum?.generateStudentId()
        if (studentNum == null) {
            throw RuntimeException("Student number is null for user with email: $email")
        }
        if (studentNum <= 1100 || studentNum >= 3419) {
            throw RuntimeException("Student number out of valid range (1101-3418): $studentNum")
        }
        return studentNum
    }
}