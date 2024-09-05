package Rainbow_Frends.global.auth

import Rainbow_Frends.global.security.jwt.JwtProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class GetUser(private val jwtProvider: JwtProvider) {
    fun getUser(request: HttpServletRequest): UserDetails {
        val accessToken = jwtProvider.resolveToken(request)
        val authentication = jwtProvider.getAuthentication(accessToken!!)
        val userDetails = authentication.principal as UserDetails
        return userDetails
    }
}