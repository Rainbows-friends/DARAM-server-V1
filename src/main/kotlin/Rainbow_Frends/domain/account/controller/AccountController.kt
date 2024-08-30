package Rainbow_Frends.domain.account.controller

import Rainbow_Frends.domain.User.repository.UserRepository
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.account.service.ProfilePictureService
import Rainbow_Frends.global.security.jwt.JwtProvider
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "계정", description = "계정정보 관련 API")
@RequestMapping("/api/account")
class AccountController(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository,
    private val profilePictureService: ProfilePictureService,
    private val jwtProvider: JwtProvider
) {

    @Operation(summary = "프로필 사진 업데이트 API", description = "사용자의 프로필 사진을 업로드하거나 기존 사진을 업데이트하는 API")
    @ResponseStatus(HttpStatus.CREATED)
    @PatchMapping("/profile-picture")
    fun updateProfilePicture(
        @RequestParam("file") file: MultipartFile, request: HttpServletRequest
    ): ResponseEntity<String> {
        val accessToken = jwtProvider.resolveToken(request)
        val authentication = jwtProvider.getAuthentication(accessToken!!)
        val userDetails = authentication.principal as UserDetails
        val email = userDetails.username
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("이메일로 사용자를 찾을 수 없습니다: $email")
        val studentNum = user.studentNum?.let {
            (it.grade * 1000) + (it.classNum * 100) + it.number
        } ?: throw RuntimeException("유효하지 않은 학번 정보입니다.")
        val account = accountRepository.findByStudentId(studentNum)
            ?: throw RuntimeException("학번 $studentNum 에 해당하는 Account를 찾을 수 없습니다.")
        profilePictureService.updateProfilePicture(account, file)
        return ResponseEntity.status(HttpStatus.CREATED).body("Profile picture updated successfully.")
    }
}