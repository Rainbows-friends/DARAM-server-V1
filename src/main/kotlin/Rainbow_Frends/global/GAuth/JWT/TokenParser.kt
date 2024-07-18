package Rainbow_Frends.global.GAuth.JWT

import Rainbow_Frends.global.GAuth.JWT.JwtProperties.ACCESS_SECRET
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import java.security.Key

object TokenParser {

    fun getTokenSubjectOrNull(token: String): String? {
        return try {
            getTokenBody(token, ACCESS_SECRET).subject
        } catch (e: ExpiredJwtException) {
            null
        }
    }

    private fun getTokenBody(token: String, secret: Key): Claims {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).body
    }
}