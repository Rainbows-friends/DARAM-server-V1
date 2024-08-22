package Rainbow_Frends.global.security.jwt

import Rainbow_Frends.domain.account.exception.ExpiredTokenException
import Rainbow_Frends.domain.account.exception.InvalidTokenException
import Rainbow_Frends.domain.account.presentation.dto.response.TokenResponse
import Rainbow_Frends.global.auth.AuthDetailsService
import Rainbow_Frends.global.exception.DARAMException
import Rainbow_Frends.global.exception.ErrorCode
import Rainbow_Frends.global.redis.RedisConfig
import Rainbow_Frends.global.security.filter.JwtFilter.Companion.AUTHORIZATION_HEADER
import Rainbow_Frends.global.security.filter.JwtFilter.Companion.BEARER_PREFIX
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.security.Key
import java.time.LocalDateTime
import java.util.*


@Component
class JwtProvider(
    private val authDetailsService: AuthDetailsService,
    private val redisUtil: RedisConfig.RedisUtil,
) {
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String
    private lateinit var key: Key

    companion object {
        private const val AUTHORITIES_KEY = "auth"
        private const val BEARER_TYPE = "Bearer "
        private const val ACCESS_TOKEN_TIME = 60L * 30 * 4
        private const val REFRESH_TOKEN_TIME = 60L * 60 * 24 * 7
    }

    @PostConstruct
    fun init() {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateTokenDto(id: UUID): TokenResponse {
        val accessToken = generateAccessToken(id)
        val refreshToken = generateRefreshToken(id)
        val accessTokenExpiresIn = LocalDateTime.now().plusSeconds(ACCESS_TOKEN_TIME)
        val refreshTokenExpiresIn = LocalDateTime.now().plusSeconds(REFRESH_TOKEN_TIME)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = accessTokenExpiresIn,
            refreshTokenExpiresIn = refreshTokenExpiresIn
        )
    }

    fun getExpiration(accessToken: String): Long {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body

        return claims.expiration.time
    }


    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            !redisUtil.hasKeyBlackList(token)
        } catch (e: ExpiredJwtException) {
            throw ExpiredTokenException()
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    fun getAuthentication(accessToken: String): Authentication {
        val claims = parseClaims(accessToken)

        if (claims[AUTHORITIES_KEY] == null) {
            throw DARAMException(ErrorCode.INVALID_TOKEN)
        }

        val principal: UserDetails = authDetailsService.loadUserByUsername(claims.subject)
        return UsernamePasswordAuthenticationToken(principal, "", principal.authorities)
    }

    private fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            bearerToken.substring(7)
        } else null
    }

    fun parseRefreshToken(refreshToken: String): String? {
        return if (refreshToken.startsWith(BEARER_TYPE)) {
            refreshToken.replace(BEARER_TYPE, "")
        } else null
    }

    fun generateAccessToken(id: UUID): String {
        val now = Date().time

        val accessTokenExpiresIn = Date(now + ACCESS_TOKEN_TIME)

        return Jwts.builder().setSubject(id.toString()).claim(AUTHORITIES_KEY, "JWT").setIssuedAt(Date())
            .setExpiration(accessTokenExpiresIn).signWith(key, SignatureAlgorithm.HS256).compact()
    }

    fun generateRefreshToken(id: UUID): String {
        val now = Date().time

        val refreshTokenExpiresIn = Date(now + REFRESH_TOKEN_TIME)

        return Jwts.builder().setSubject(id.toString()).setExpiration(refreshTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256).compact()
    }
}