package Rainbow_Frends.domain.GAuth.JWT

import Rainbow_Frends.domain.GAuth.GAuthRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val gauthRepository: GAuthRepository
) : OncePerRequestFilter() {

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
                val authentication =
                    gauthUser.id?.let { gauthUser.role?.let { it1 -> CustomGAuthUser(it, it1) } }?.let {
                        CustomAuthenticationToken(
                            listOf(SimpleGrantedAuthority(gauthUser.role)), it
                        )
                    }
                if (authentication != null) {
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                }
                SecurityContextHolder.getContext().authentication = authentication
                println("JWT Filter: 인증 성공 - 사용자 ID: $userId")
            } else {
                println("JWT Filter: 유효하지 않은 JWT 토큰")
            }
        } else {
            println("JWT Filter: JWT 토큰이 없음 또는 잘못된 형식")
        }

        filterChain.doFilter(request, response)
    }
}