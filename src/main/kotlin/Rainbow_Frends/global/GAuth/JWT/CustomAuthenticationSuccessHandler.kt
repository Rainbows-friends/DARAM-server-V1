package Rainbow_Frends.global.GAuth.JWT

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import dev.yangsijun.gauth.authentication.GAuthAuthenticationToken
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.io.IOException

class CustomAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    private val logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler::class.java)

    @Throws(IOException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication
    ) {
        try {
            val gauthAuthToken = authentication as? GAuthAuthenticationToken
                ?: throw IllegalArgumentException("Invalid authentication token")
            val user = gauthAuthToken.principal ?: throw IllegalArgumentException("Invalid principal")

            val key = authentication.name
            val userId = user.getAttribute<Long>(key) ?: throw IllegalArgumentException("User ID not found")

            val token = TokenGenerator.generateToken(userId)
            sendTokenResponse(response, token)
        } catch (e: Exception) {
            logger.error("Authentication success handling failed", e)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication failed")
        }
    }

    @Throws(IOException::class)
    private fun sendTokenResponse(response: HttpServletResponse, token: TokenResponse) {
        response.characterEncoding = "utf-8"
        response.status = HttpServletResponse.SC_OK
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val objectMapper = ObjectMapper().apply {
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }

        response.writer.write(objectMapper.writeValueAsString(token))
    }
}