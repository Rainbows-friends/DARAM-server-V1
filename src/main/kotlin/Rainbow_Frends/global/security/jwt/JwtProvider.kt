package Rainbow_Frends.global.security.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class JwtProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    private val authDetailsService: AuthDetailsService,
    private val redisUtil: RedisUtil
) {
    private lateinit var key: Key

    companion object {
        private const val AUTHORITIES_KEY = "auth"
        private const val BEARER_TYPE = "Bearer "
        private const val ACCESS_TOKEN_TIME = 1000 * 60 * 30L
        private const val REFRESH_TOKEN_TIME = 1000 * 60 * 60 * 24 * 7L
    }

    @PostConstruct
    fun init() {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateTokenDto(id: UUID): TokenResponse {
        return TokenResponse.builder()
            .accessToken(generateAccessToken(id))
            .refreshToken(generateRefreshToken(id))
            .accessTokenExpiresIn(LocalDateTime.now().plusSeconds(ACCESS_TOKEN_TIME))
            .refreshTokenExpiresIn(LocalDateTime.now().plusSeconds(REFRESH_TOKEN_TIME))
            .build()
    }

    fun getExpiration(accessToken: String): Long {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(accessToken)
            .body

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
            throw MindWayException(ErrorCode.INVALID_TOKEN)
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

        return Jwts.builder()
            .setSubject(id.toString())
            .claim(AUTHORITIES_KEY, "JWT")
            .setIssuedAt(Date())
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateRefreshToken(id: UUID): String {
        val now = Date().time
        val refreshTokenExpiresIn = Date(now + REFRESH_TOKEN_TIME)

        return Jwts.builder()
            .setSubject(id.toString())
            .setExpiration(refreshTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
}