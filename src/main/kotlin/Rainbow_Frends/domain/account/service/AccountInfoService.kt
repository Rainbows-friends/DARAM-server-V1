package Rainbow_Frends.domain.account.service

import Rainbow_Frends.domain.account.service.impl.AccountInfoServiceImpl

interface AccountInfoService {
    fun getUserInfomation(email: String): AccountInfoServiceImpl.UserInfo
    fun getAccountInfomation(grade: Byte, classNum: Byte, number: Byte): AccountInfoServiceImpl.AccountInfo?
}