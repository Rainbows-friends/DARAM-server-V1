package Rainbow_Frends.domain.account.service.impl

import Rainbow_Frends.domain.account.exception.UserNotFoundException
import Rainbow_Frends.domain.account.presentation.dto.AccountInfo
import Rainbow_Frends.domain.account.presentation.dto.UserInfo
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.account.service.AccountInfoService
import Rainbow_Frends.domain.user.entity.User
import Rainbow_Frends.domain.user.repository.UserRepository
import Rainbow_Frends.global.annotation.ServiceWithTransaction
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable

@ServiceWithTransaction
class AccountInfoServiceImpl(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository,
    @Value("\${CLOUDFLARE_BUCKET_SUBDOMAIN}") private val bucketSubdomain: String
) : AccountInfoService {

    private val logger = LoggerFactory.getLogger(AccountInfoServiceImpl::class.java)

    @Cacheable(
        value = ["accountCache"],
        key = "T(java.lang.String).valueOf(#grade) + '-' + T(java.lang.String).valueOf(#classNum) + '-' + T(java.lang.String).valueOf(#number)",
        cacheManager = "cacheManager",
        condition = "#grade != null && #classNum != null && #number != null"
    )
    override fun getAccountInfomation(grade: Byte, classNum: Byte, number: Byte): AccountInfo? {
        val studentNum: Int = ((grade * 1000) + (classNum * 100) + number)
        val accountinfo = accountRepository.findByStudentId(studentNum)
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
    }

    @Cacheable(value = ["userCache"], key = "#email", cacheManager = "cacheManager", condition = "#email != null")
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

    @CachePut(value = ["accountCache"], key = "#studentNum")
    fun updateAccountInformation(accountInfo: AccountInfo, studentNum: Int): AccountInfo {
        val account = accountRepository.findByStudentId(studentNum) ?: throw UserNotFoundException()
        accountRepository.save(account)
        return accountInfo
    }

    @CacheEvict(value = ["accountCache"], key = "#studentNum")
    fun evictAccountCache(studentNum: Int) {
        logger.info("Evicted account cache for student number: $studentNum")
    }
}