import Rainbow_Frends.domain.GAuth.GAuth
import Rainbow_Frends.domain.GAuth.GAuthRepository
import Rainbow_Frends.domain.GAuth.JWT.CustomAuthenticationToken
import Rainbow_Frends.domain.GAuth.JWT.CustomGAuthUser
import Rainbow_Frends.domain.GAuth.JWT.JwtProperties
import Rainbow_Frends.domain.GAuth.JWT.TokenParser
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JwtAuthenticationFilter(
    private val gauthRepository: GAuthRepository
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val jwtTokenHeader = request.getHeader(JwtProperties.HEADER)

        if (shouldSkipAuthentication(jwtTokenHeader)) {
            super.doFilter(request, response, filterChain)
            return
        }

        val accessToken = extractAccessToken(jwtTokenHeader)
        val userId = getUserIdFromToken(accessToken)
        val gauthUser = getGAuthUserById(userId)

        val authentication = createAuthenticationToken(gauthUser)

        SecurityContextHolder.clearContext()
        SecurityContextHolder.getContext().authentication = authentication

        super.doFilter(request, response, filterChain)
    }

    private fun shouldSkipAuthentication(jwtTokenHeader: String?): Boolean {
        return jwtTokenHeader == null || !jwtTokenHeader.startsWith(JwtProperties.TOKEN_PREFIX)
    }

    private fun extractAccessToken(jwtTokenHeader: String): String {
        return jwtTokenHeader.replace(JwtProperties.TOKEN_PREFIX, "")
    }

    private fun getUserIdFromToken(accessToken: String): Long {
        val userId = TokenParser.getTokenSubjectOrNull(accessToken)
            ?: throw PreAuthenticatedCredentialsNotFoundException("만료되거나 유효하지 않은 JWT")
        return userId.toLong()
    }

    private fun getGAuthUserById(userId: Long): GAuth {
        return gauthRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("요청한 사용자 ID와 일치하는 사용자를 찾을 수 없습니다") }
    }

    private fun createAuthenticationToken(gauthUser: GAuth): CustomAuthenticationToken? {
        val authorities = listOf(SimpleGrantedAuthority(gauthUser.role))
        val customUser = gauthUser.id?.let { gauthUser.role?.let { it1 -> CustomGAuthUser(it, it1) } }
        return customUser?.let { CustomAuthenticationToken(authorities, it) }
    }
}