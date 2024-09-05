package Rainbow_Frends.domain.account.service.impl

import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.account.service.ProfilePictureService
import Rainbow_Frends.global.annotation.ServiceWithTransaction
import Rainbow_Frends.global.auth.GetStudentId
import Rainbow_Frends.global.auth.GetUser
import Rainbow_Frends.global.aws.service.FileDeleteService
import Rainbow_Frends.global.aws.service.FileUploadService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile

@ServiceWithTransaction
class ProfilePictureServiceImpl(
    private val accountRepository: AccountRepository,
    private val fileDeleteService: FileDeleteService,
    private val fileUploadService: FileUploadService,
    @Value("\${AWS_S3_BUCKET}") private val bucket: String,
    private val getUser: GetUser,
    private val getStudentId: GetStudentId
) : ProfilePictureService {

    override fun updateProfilePicture(request: HttpServletRequest, file: MultipartFile) {
        val studentNum = getStudentId.getStudentId(getUser.getUser(request).username)
        val account = accountRepository.findByStudentId(studentNum)
            ?: throw RuntimeException("학번 $studentNum 에 해당하는 Account를 찾을 수 없습니다.")
        account.profilePictureURL?.let { existingProfilePictureUrl ->
            fileDeleteService.deleteFile(existingProfilePictureUrl)
        }
        val (newProfilePictureUrl, newProfilePictureName) = fileUploadService.uploadFile(file, bucket)
        account.profilePictureURL = newProfilePictureUrl
        account.profilePictureName = newProfilePictureName
        accountRepository.save(account)
    }
}