package Rainbow_Frends.global.security.filter

import Rainbow_Frends.global.security.jwt.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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

    private val excludedPaths = setOf(
        "/gauth/authorization",
        "/api/login/gauth/code",
        "/api/login/gauth/logout",
        "/api/login/gauth/reissue",
        "/page",
        "/actuator/health"
    )

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val shouldNotFilter = excludedPaths.any { request.requestURI.startsWith(it) }
        logger.debug("Request URI: ${request.requestURI}, Should not filter: $shouldNotFilter")
        return shouldNotFilter
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