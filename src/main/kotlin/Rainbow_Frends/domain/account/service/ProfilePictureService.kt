package Rainbow_Frends.domain.account.service

import Rainbow_Frends.domain.account.entity.Account
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.multipart.MultipartFile

interface ProfilePictureService {
    fun updateProfilePicture(request: HttpServletRequest, file: MultipartFile)
}