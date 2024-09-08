package Rainbow_Frends.domain.account.service

import Rainbow_Frends.domain.account.presentation.dto.AccountInfo
import Rainbow_Frends.domain.account.presentation.dto.UserInfo

interface AccountInfoService {
    fun getUserInfomation(email: String): UserInfo
    fun getAccountInfomation(grade: Byte, classNum: Byte, number: Byte): AccountInfo?
}