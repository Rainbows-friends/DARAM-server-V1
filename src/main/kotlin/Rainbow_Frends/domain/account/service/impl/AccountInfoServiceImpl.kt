package Rainbow_Frends.domain.account.service.impl

import Rainbow_Frends.domain.User.entity.Authority
import Rainbow_Frends.domain.User.repository.UserRepository
import Rainbow_Frends.domain.account.entity.Role
import Rainbow_Frends.domain.account.exception.UserNotFoundException
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.account.service.AccountInfoService
import Rainbow_Frends.global.annotation.ServiceWithTransaction
import org.springframework.beans.factory.annotation.Value

@ServiceWithTransaction
class AccountInfoServiceImpl(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository,
    @Value("\${CLOUDFLARE_BUCKET_SUBDOMAIN}") private val bucketSubdomain: String
) : AccountInfoService {
    override fun getAccountInfomation(grade: Byte, classNum: Byte, number: Byte): AccountInfo {
        val studentNum: String = (grade * 1000).toString() + (classNum * 100).toString() + (number.toString())
        val accountinfo = accountRepository.findByStudentId(studentNum.toInt())
        if (accountinfo == null) {
            throw UserNotFoundException()
        }
        accountinfo.profilePictureURL = bucketSubdomain + "/" + accountinfo.profilePictureName
        return AccountInfo(
            accountinfo.studentId!!.toShort(),
            accountinfo.profilePictureName,
            accountinfo.profilePictureURL,
            accountinfo.role
        )
    }

    override fun getUserInfomation(email: String): UserInfo {
        val userinfo = userRepository.findByEmail(email)
        if (userinfo == null) {
            throw UserNotFoundException()
        }
        return UserInfo(
            userinfo.authority,
            userinfo.email,
            userinfo.name,
            userinfo.studentNum?.grade!!.toByte(),
            userinfo.studentNum.classNum.toByte(),
            userinfo.studentNum.number.toByte(),
        )
    }

    data class UserInfo(
        val gauthAuthority: Authority?,
        val email: String?,
        val name: String?,
        val grade: Byte,
        val classNum: Byte,
        val number: Byte
    )

    data class AccountInfo(
        val studentNum: Short, val profilePictureName: String?, val profilePictureUrl: String?, val daramRole: Role?
    )
}