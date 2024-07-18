package Rainbow_Frends.global.GAuth.JWT

import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey

object JwtProperties {
    const val HEADER: String = "Authorization"
    const val TOKEN_PREFIX: String = "Bearer "
    const val ACCESS_EXP: Int = 86400
    const val ACCESS_SECRET_VALUE: String = "qwertyuiopasdfghjklzxcvbnm1234567890qwertyuiopasdfghjklzxcvbnm1234567890"
    val ACCESS_SECRET: SecretKey = Keys.hmacShaKeyFor(ACCESS_SECRET_VALUE.toByteArray(StandardCharsets.UTF_8))
}