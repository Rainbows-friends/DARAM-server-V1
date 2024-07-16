package Rainbow_Frends.domain.GAuth.JWT

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

object TokenGenerator {

    fun generateToken(userId: Long): TokenResponse {
        return TokenResponse(
            generateAccessToken(userId)
        )
    }

    private fun generateAccessToken(userId: Long): String {
        val issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS)
        val expiration = issuedAt.plus(JwtProperties.ACCESS_EXP.toLong(), ChronoUnit.SECONDS)
        return Jwts.builder().signWith(JwtProperties.ACCESS_SECRET, SignatureAlgorithm.HS256)
            .setSubject(userId.toString()).setIssuedAt(Date.from(issuedAt)).setExpiration(Date.from(expiration))
            .compact()
    }
}