package Rainbow_Frends.global.security.filter

import Rainbow_Frends.global.security.jwt.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JwtFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer "
    }

    private val logger = LoggerFactory.getLogger(JwtFilter::class.java)

    private val excludedPaths = setOf(
        "/gauth/authorization", "/login/gauth/code", "/login/gauth/logout", "/login/gauth/reissue"
    )

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludedPaths.contains(request.requestURI)
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val jwt = jwtProvider.resolveToken(request)
        if (StringUtils.hasText(jwt) && jwt?.let { jwtProvider.validateToken(it) } == true) {
            val authentication: Authentication = jwtProvider.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
        } else if (!StringUtils.hasText(jwt)) {
            logger.warn("JWT 토큰이 제공되지 않았습니다. 요청 URL: ${request.requestURI}")
        }
        filterChain.doFilter(request, response)
    }
}