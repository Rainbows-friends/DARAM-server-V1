package Rainbow_Frends.domain.account.service.impl

import Rainbow_Frends.domain.User.entity.Authority
import Rainbow_Frends.domain.User.entity.User
import Rainbow_Frends.domain.User.repository.UserRepository
import Rainbow_Frends.domain.account.entity.Role
import Rainbow_Frends.domain.account.exception.UserNotFoundException
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.account.service.AccountInfoService
import Rainbow_Frends.global.annotation.ServiceWithTransaction
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value

@ServiceWithTransaction
class AccountInfoServiceImpl(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository,
    @Value("\${CLOUDFLARE_BUCKET_SUBDOMAIN}") private val bucketSubdomain: String
) : AccountInfoService {

    private val logger = LoggerFactory.getLogger(AccountInfoServiceImpl::class.java)

    override fun getAccountInfomation(grade: Byte, classNum: Byte, number: Byte): AccountInfo? {
        val studentNum: String = ((grade * 1000) + (classNum * 100)+ number).toString()
        val accountinfo = accountRepository.findByStudentId(studentNum.toInt())
        if (accountinfo == null) {
            logger.error("Account not found for student number: $studentNum")
            throw UserNotFoundException()
        }
        accountinfo.profilePictureURL = "$bucketSubdomain/${accountinfo.profilePictureName}"
        return AccountInfo(
            accountinfo.studentId!!.toShort(),
            accountinfo.profilePictureName,
            accountinfo.profilePictureURL,
            accountinfo.role,
            accountinfo.lateNumber,
            accountinfo.roomNumber,
            accountinfo.floor
        )
        return null
    }

    override fun getUserInfomation(email: String): UserInfo {
        val userinfo: User? = userRepository.findByEmail(email)
        if (userinfo == null) {
            logger.error("User not found with email: $email")
            throw UserNotFoundException()
        }

        val studentNum = userinfo.studentNum ?: throw UserNotFoundException()


        return UserInfo(
            userinfo.authority,
            userinfo.email,
            userinfo.name,
            studentNum.grade.toByte(),
            studentNum.classNum.toByte(),
            studentNum.number.toByte()
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
        val studentNum: Short,
        val profilePictureName: String?,
        val profilePictureUrl: String?,
        val daramRole: Role?,
        val lateNumber: Int?,
        val roomNumber: Int?,
        val floor: Int?
    )
}