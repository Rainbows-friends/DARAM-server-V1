package Rainbow_Frends.global.GAuth.JWT

import Rainbow_Frends.global.GAuth.GAuthService
import gauth.GAuthUserInfo
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.server.ResponseStatusException

class GauthAuthenticationFilter(
    url: String,
    authManager: AuthenticationManager,
    private val jwtUtil: JWTUtil,
    private val gAuthService: GAuthService
) : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher(url)) {

    init {
        authenticationManager = authManager
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        request: HttpServletRequest, response: HttpServletResponse
    ): Authentication {
        val accessCode = request.getParameter("accessCode")
        return try {
            val userInfo: GAuthUserInfo = gAuthService.loginWithGauth(accessCode)
            UsernamePasswordAuthenticationToken(userInfo, userInfo.id)
        } catch (e: ResponseStatusException) {
            throw AuthenticationException("Authentication failed by ${e.statusCode} ${e.message}") {}
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication
    ) {
        val userInfo = authResult.principal as GAuthUserInfo
        val jwt = jwtUtil.createJwt(userInfo.name, userInfo.id, 86400000L)
        response.addHeader("Authorization", "Bearer $jwt")
    }
}