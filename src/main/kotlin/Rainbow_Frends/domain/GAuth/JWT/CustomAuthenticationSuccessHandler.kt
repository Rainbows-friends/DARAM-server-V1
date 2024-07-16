import Rainbow_Frends.domain.GAuth.JWT.TokenGenerator
import Rainbow_Frends.domain.GAuth.JWT.TokenResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import dev.yangsijun.gauth.authentication.GAuthAuthenticationToken
import dev.yangsijun.gauth.core.user.GAuthUser
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.io.IOException

class CustomAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    @Throws(IOException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication
    ) {
        val gauthAuthToken = authentication as GAuthAuthenticationToken
        val user = gauthAuthToken.principal as GAuthUser
        val key = authentication.name
        val userId = user.getAttribute<Long>(key)

        val token = userId?.let { TokenGenerator.generateToken(it) }
        token?.let { sendTokenResponse(response, it) }
    }

    @Throws(IOException::class)
    private fun sendTokenResponse(response: HttpServletResponse, token: TokenResponse) {
        response.characterEncoding = "utf-8"
        response.status = HttpServletResponse.SC_OK
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        response.writer.write(objectMapper.writeValueAsString(token))
    }
}