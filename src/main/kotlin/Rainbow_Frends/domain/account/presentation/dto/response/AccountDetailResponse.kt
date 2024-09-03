package Rainbow_Frends.domain.account.presentation.dto.response

import Rainbow_Frends.domain.User.entity.Authority
import Rainbow_Frends.domain.account.entity.Role

data class AccountDetailResponse(
    val gauthAuthority: Authority?,
    val email: String?,
    val name: String?,
    val grade: Byte,
    val classNum: Byte,
    val number: Byte,
    val studentId: Short,
    val profilePictureName: String? = null,
    val profilePictureUrl: String? = null,
    val daramRole: Role? = null,
    val lateNumber: Int? = 0,
    val roomNumber: Int? = null,
    val floor: Byte? = null
)