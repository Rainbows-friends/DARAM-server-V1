package Rainbow_Frends.global.cache

import Rainbow_Frends.domain.account.exception.UserNotFoundException
import Rainbow_Frends.domain.account.repository.jpa.AccountRepository
import Rainbow_Frends.domain.account.service.AccountInfoService
import Rainbow_Frends.domain.user.repository.UserRepository
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CacheInitializer(
    private val accountInfoService: AccountInfoService,
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) {

    private val log = LoggerFactory.getLogger(CacheInitializer::class.java)

    @PostConstruct
    fun initializeCache() {
        try {
            userRepository.findAll().forEach { user ->
                val studentNum = user.studentNum ?: return@forEach
                try {
                    val accountInfo = accountInfoService.getAccountInfomation(
                        studentNum.grade.toByte(), studentNum.classNum.toByte(), studentNum.number.toByte()
                    )
                    if (accountInfo != null) {
                        log.info("Added to cache: Grade-${studentNum.grade}, Class-${studentNum.classNum}, Number-${studentNum.number}")
                    } else {
                        log.warn("Account information not found: Grade-${studentNum.grade}, Class-${studentNum.classNum}, Number-${studentNum.number}")
                    }
                } catch (e: UserNotFoundException) {
                    log.error("User information not found: ${user.email}")
                } catch (e: Exception) {
                    log.error("An error occurred (User): ${e.message}")
                }
            }

            accountRepository.findAll().forEach { account ->
                try {
                    val accountInfo = accountInfoService.getAccountInfomation(
                        account.studentId!!.div(1000).toByte(),
                        (account.studentId!!.div(100) % 10).toByte(),
                        (account.studentId!! % 100).toByte()
                    )
                    if (accountInfo != null) {
                        log.info(
                            "Added to cache: Grade-${account.studentId!!.div(1000)}, Class-${
                                (account.studentId!!.div(
                                    100
                                ) % 10)
                            }, Number-${(account.studentId!! % 100)}"
                        )
                    } else {
                        log.warn("Account information not found for student ID: ${account.studentId}")
                    }
                } catch (e: UserNotFoundException) {
                    log.error("Student information not found for student ID: ${account.studentId}")
                } catch (e: Exception) {
                    log.error("An error occurred (Account): ${e.message}")
                }
            }
            log.info("Cache initialized successfully.")
        } catch (e: Exception) {
            log.error("Error during cache initialization: ${e.message}")
        }
    }
}