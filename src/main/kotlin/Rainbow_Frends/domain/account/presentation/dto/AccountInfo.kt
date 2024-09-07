package Rainbow_Frends.domain.account.presentation.dto

import Rainbow_Frends.domain.account.entity.Role

data class AccountInfo(
    val studentNum: Short,
    val profilePictureName: String?,
    val profilePictureUrl: String?,
    val daramRole: Role?,
    val lateNumber: Int?,
    val roomNumber: Int?,
    val floor: Int?
)