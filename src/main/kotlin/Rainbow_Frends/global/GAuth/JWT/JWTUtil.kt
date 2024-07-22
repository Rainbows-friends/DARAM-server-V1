package Rainbow_Frends.global.GAuth.JWT

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JWTUtil(@Value("\${jwt.secret}") secret: String) {
    private val secretKey: SecretKey =
        SecretKeySpec(secret.toByteArray(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.jcaName)

    fun getUsername(token: String): String? {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body.subject
    }

    fun getUserId(token: String): Long? {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
            .parseClaimsJws(token).body["userId", Long::class.java]
    }

    fun isExpired(token: String): Boolean {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
            .parseClaimsJws(token).body.expiration.before(Date())
    }

    fun createJwt(username: String, userId: Long, expiredMs: Long): String {
        return Jwts.builder().setSubject(username).claim("userId", userId).setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expiredMs)).signWith(secretKey).compact()
    }
}