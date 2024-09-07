package Rainbow_Frends.domain.account.presentation.dto

import Rainbow_Frends.domain.user.entity.Authority

data class UserInfo(
    val gauthAuthority: Authority?,
    val email: String?,
    val name: String?,
    val grade: Byte,
    val classNum: Byte,
    val number: Byte
)