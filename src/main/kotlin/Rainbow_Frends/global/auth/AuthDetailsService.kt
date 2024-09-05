package Rainbow_Frends.global.auth

import Rainbow_Frends.domain.user.repository.UserRepository
import Rainbow_Frends.global.exception.DARAMException
import Rainbow_Frends.global.exception.ErrorCode
import Rainbow_Frends.global.log.logger
import org.slf4j.Logger
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    private val logger: Logger = (this as Any).logger()

    override fun loadUserByUsername(id: String): UserDetails {
        logger.info("Loading User by ID: $id")
        return userRepository.findById(UUID.fromString(id)).map { AuthDetails(it) as UserDetails }.orElseThrow {
            logger.error("User not found for ID: $id")
            throw DARAMException(ErrorCode.USER_NOT_FOUND)
        }
    }
}