package Rainbow_Frends.domain.account.service

import Rainbow_Frends.domain.account.entity.Account
import org.springframework.web.multipart.MultipartFile

interface ProfilePictureService {
    fun updateProfilePicture(account: Account, file: MultipartFile)
}