package Rainbow_Frends.global.GAuth.JWT

import Rainbow_Frends.global.GAuth.GAuthRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val gauthRepository: GAuthRepository
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val jwtTokenHeader = request.getHeader(JwtProperties.HEADER)
        if (jwtTokenHeader != null && jwtTokenHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            val accessToken = jwtTokenHeader.replace(JwtProperties.TOKEN_PREFIX, "")
            val userId = TokenParser.getTokenSubjectOrNull(accessToken)
            if (userId != null) {
                val gauthUser = gauthRepository.findById(userId.toLong())
                    .orElseThrow { IllegalArgumentException("요청한 사용자 ID와 일치하는 사용자를 찾을 수 없습니다") }
                if (gauthUser.id != null && gauthUser.role != null) {
                    val authentication = CustomAuthenticationToken(
                        listOf(SimpleGrantedAuthority(gauthUser.role)), CustomGAuthUser(gauthUser.id, gauthUser.role)
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                    logger.info("JWT Filter: 인증 성공 - 사용자 ID: $userId")

                } else {
                    logger.warn("JWT Filter: 사용자 ID나 역할이 null입니다.")
                }
            } else {
                logger.warn("JWT Filter: 유효하지 않은 JWT 토큰")
            }
        } else {
            logger.warn("JWT Filter: JWT 토큰이 없음 또는 잘못된 형식")
        }
        filterChain.doFilter(request, response)
    }
}