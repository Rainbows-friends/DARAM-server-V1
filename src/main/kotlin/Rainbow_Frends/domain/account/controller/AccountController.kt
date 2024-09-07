package Rainbow_Frends.domain.account.controller

import Rainbow_Frends.domain.account.presentation.dto.AccountInfo
import Rainbow_Frends.domain.account.presentation.dto.UserInfo
import Rainbow_Frends.domain.account.presentation.dto.response.AccountDetailResponse
import Rainbow_Frends.domain.account.service.AccountInfoService
import Rainbow_Frends.domain.account.service.ProfilePictureService
import Rainbow_Frends.global.auth.GetUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "계정", description = "계정정보 관련 API")
@RequestMapping("/api/account")
class AccountController(
    private val profilePictureService: ProfilePictureService,
    private val accountInfoService: AccountInfoService,
    private val getUser: GetUser
) {
    private val logger = LoggerFactory.getLogger(AccountController::class.java)

    @Operation(summary = "프로필 사진 업데이트 API", description = "사용자의 프로필 사진을 업로드하거나 기존 사진을 업데이트하는 API")
    @ResponseStatus(HttpStatus.CREATED)
    @PatchMapping("/profile-picture")
    fun updateProfilePicture(
        @RequestParam("file") file: MultipartFile, request: HttpServletRequest
    ): ResponseEntity<String> {
        profilePictureService.updateProfilePicture(request, file)
        return ResponseEntity.status(HttpStatus.CREATED).body("Profile picture updated successfully.")
    }

    @Operation(summary = "프로필 사진 삭제 API", description = "사용자의 프로필 사진을 삭제하는 API")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/profile-picture")
    fun deleteProfilePicture(request: HttpServletRequest): ResponseEntity<String> {
        profilePictureService.deleteProfilePicture(request)
        return ResponseEntity.status(HttpStatus.OK).body("Profile picture delete successfully")
    }

    @Operation(summary = "계정 정보 조회 API", description = "사용자의 계정정보를 조회하는 API")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    fun getAccountDetails(request: HttpServletRequest): AccountDetailResponse? {
        val email = getUser.getUser(request).username
        logger.info(email)
        val user: UserInfo = accountInfoService.getUserInfomation(email)
        val account: AccountInfo? = accountInfoService.getAccountInfomation(user.grade, user.classNum, user.number)
        val response = account?.let {
            AccountDetailResponse(
                user.gauthAuthority,
                user.email,
                user.name,
                user.grade,
                user.classNum,
                user.number,
                it.studentNum,
                account.profilePictureName,
                account.profilePictureUrl,
                account.daramRole,
                account.lateNumber,
                account.roomNumber,
                account.floor?.toByte()
            )
        }
        return response
    }
}