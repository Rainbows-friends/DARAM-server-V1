package Rainbow_Frends.domain.auth.controller

import Rainbow_Frends.domain.account.presentation.dto.request.SignInRequest
import Rainbow_Frends.domain.account.presentation.dto.response.TokenResponse
import Rainbow_Frends.domain.account.service.LogoutService
import Rainbow_Frends.domain.account.service.ReissueTokenService
import Rainbow_Frends.domain.account.service.SignInService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/login/gauth")
@RestController
class AuthController(
    private val signInService: SignInService,
    private val logoutService: LogoutService,
    private val reissueTokenService: ReissueTokenService
) {

    @Value("\${GAuth-CLIENT-ID}")
    private lateinit var clientId: String

    @Value("\${GAuth-CLIENT-SECRET}")
    private lateinit var clientSecret: String

    @Value("\${GAuth-REDIRECT-URI}")
    private lateinit var redirectUri: String

    @PostMapping("/code")
    fun signIn(@RequestBody @Valid signInRequest: SignInRequest): ResponseEntity<TokenResponse> {
        val response = signInService.execute(signInRequest)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/reissue")
    fun reissueToken(@RequestHeader("Authorization") refreshToken: String): ResponseEntity<TokenResponse> {
        val tokenResponse: TokenResponse = reissueTokenService.execute(refreshToken)
        return ResponseEntity.ok(tokenResponse)
    }

    @DeleteMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Void> {
        logoutService.execute(request.getHeader("Authorization").substring(7))
        return ResponseEntity.noContent().build()
    }
}