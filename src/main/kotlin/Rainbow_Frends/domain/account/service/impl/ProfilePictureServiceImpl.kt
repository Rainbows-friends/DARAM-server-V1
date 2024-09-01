package Rainbow_Frends.domain.account.service.impl

import Rainbow_Frends.domain.account.entity.Account
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.account.service.ProfilePictureService
import Rainbow_Frends.global.annotation.ServiceWithTransaction
import Rainbow_Frends.global.aws.service.FileDeleteService
import Rainbow_Frends.global.aws.service.FileUploadService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile

@ServiceWithTransaction
class ProfilePictureServiceImpl(
    private val accountRepository: AccountRepository,
    private val fileDeleteService: FileDeleteService,
    private val fileUploadService: FileUploadService,
    @Value("\${AWS_S3_BUCKET}") private val bucket: String
) : ProfilePictureService {
    override fun updateProfilePicture(account: Account, file: MultipartFile) {
        val existingAccount = account.studentId?.let { accountRepository.findByStudentId(it) }
            ?: throw RuntimeException("student_id: ${account.studentId}에 해당하는 Account를 찾을 수 없습니다.")
        existingAccount.profilePictureURL?.let { existingProfilePictureUrl ->
            fileDeleteService.deleteFile(existingProfilePictureUrl)
        }
        val (newProfilePictureUrl, newProfilePictureName) = fileUploadService.uploadFile(file, bucket)
        existingAccount.profilePictureURL = newProfilePictureUrl
        existingAccount.profilePictureName = newProfilePictureName
        accountRepository.save(existingAccount)
    }
}