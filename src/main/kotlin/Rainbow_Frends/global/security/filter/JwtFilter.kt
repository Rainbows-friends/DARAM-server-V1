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

class JwtFilter(private val jwtProvider: JwtProvider) : OncePerRequestFilter() {

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer "
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val jwt = jwtProvider.resolveToken(request)

        if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
            val authentication: Authentication = jwtProvider.getAuthentication(jwt)
            SecurityContextHolder.getContext().setAuthentication(authentication)
        }

        filterChain.doFilter(request, response)
    }
}