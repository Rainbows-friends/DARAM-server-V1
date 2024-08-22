package Rainbow_Frends.global.security.service
/*
import Rainbow_Frends.domain.account.exception.ExpiredTokenException
import Rainbow_Frends.domain.account.exception.InvalidTokenException
import Rainbow_Frends.domain.account.service.ReissueTokenService
import Rainbow_Frends.global.security.jwt.JwtProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ExpiredTokenExceptionService(
    private val jwtProvider: JwtProvider,
    private val reissueTokenService: ReissueTokenService
) {

    fun handleToken(request: HttpServletRequest): ResponseEntity<Any> {
        return try {
            val accessToken = jwtProvider.resolveToken(request)
            if (accessToken != null) {
                jwtProvider.validateToken(accessToken)
                // 유효한 토큰일 경우 수행할 작업
                ResponseEntity.ok("Token is valid")
            } else {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided")
            }
        } catch (e: ExpiredTokenException) {
            // 토큰이 만료된 경우, 리프레시 토큰으로 재발급을 유도
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired, please reissue")
        } catch (e: InvalidTokenException) {

            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token")
        }
    }
}
 */