package Rainbow_Frends.global.security.filter

import Rainbow_Frends.domain.user.repository.UserRepository
import Rainbow_Frends.global.security.jwt.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class DevFilter(
    @Value("\${DEV_KEY}") private val devKey: String,
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        val emailHeader = request.getHeader("email")
        if (authorizationHeader == devKey) {
            if (emailHeader != null) {
                val user = userRepository.findByEmail(emailHeader)
                val authentication =
                    user?.id?.let { jwtProvider.generateAccessToken(it) }?.let { jwtProvider.getAuthentication(it) }
                SecurityContextHolder.getContext().authentication = authentication
                if (user != null) {
                    response.setHeader(
                        "Authorization",
                        "Bearer " + user.id?.let { jwtProvider.generateAccessToken(it) })
                }
            } else {
                response.status = HttpServletResponse.SC_BAD_REQUEST
                response.writer.write("Missing email header")
                return
            }
        }
        filterChain.doFilter(request, response)
    }
}