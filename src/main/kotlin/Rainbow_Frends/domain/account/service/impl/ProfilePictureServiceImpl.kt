package Rainbow_Frends.domain.account.service.impl

import Rainbow_Frends.domain.account.entity.Account
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.account.service.ProfilePictureService
import Rainbow_Frends.global.annotation.ServiceWithTransaction
import Rainbow_Frends.global.auth.GetStudentId
import Rainbow_Frends.global.auth.GetUser
import Rainbow_Frends.global.aws.service.FileDeleteService
import Rainbow_Frends.global.aws.service.FileUploadService
import jakarta.servlet.http.HttpServletRequest
import org.apache.coyote.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile

@ServiceWithTransaction
class ProfilePictureServiceImpl(
    private val accountRepository: AccountRepository,
    private val fileDeleteService: FileDeleteService,
    private val fileUploadService: FileUploadService,
    @Value("\${AWS_S3_BUCKET}") private val bucket: String,
    private val getUser: GetUser,
    private val getStudentId: GetStudentId,
) : ProfilePictureService {

    private val allowedImageExtensions = listOf("jpg", "jpeg", "png", "gif")
    override fun updateProfilePicture(request: HttpServletRequest, file: MultipartFile) {
        val studentNum = getStudentId.getStudentId(getUser.getUser(request).username)
        val account = accountRepository.findByStudentId(studentNum)
            ?: throw RuntimeException("Could not find account for student number $studentNum.")
        val originalFilename = file.originalFilename ?: throw BadRequestException("File name is missing.")
        val fileExtension = originalFilename.substringAfterLast('.', "").lowercase()
        val fileNameWithoutExtension = originalFilename.substringBeforeLast('.', "").lowercase()
        if (fileNameWithoutExtension == "null" || fileNameWithoutExtension == "NULL") {
            throw BadRequestException("The file name cannot be 'null' or 'NULL'.")
        }
        if (fileExtension !in allowedImageExtensions) {
            throw BadRequestException("Invalid file extension. Allowed extensions: $allowedImageExtensions.")
        }
        account.profilePictureURL?.let { existingProfilePictureUrl ->
            fileDeleteService.deleteFile(existingProfilePictureUrl)
        }
        val (newProfilePictureUrl, newProfilePictureName) = fileUploadService.uploadFile(file, bucket)
        account.profilePictureURL = newProfilePictureUrl
        account.profilePictureName = newProfilePictureName
        accountRepository.save(account)
    }

    override fun deleteProfilePicture(request: HttpServletRequest) {
        val studentNum = getStudentId.getStudentId(getUser.getUser(request).username)
        val account: Account? = accountRepository.findByStudentId(studentNum)
        account?.let {
            it.profilePictureURL = null
            it.profilePictureName = null
            accountRepository.save(it)
        }
    }
}