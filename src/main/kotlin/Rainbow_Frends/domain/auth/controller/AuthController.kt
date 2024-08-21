package Rainbow_Frends.domain.auth.controller

import Rainbow_Frends.domain.account.presentation.dto.request.SignInRequest
import Rainbow_Frends.domain.account.presentation.dto.response.TokenResponse
import Rainbow_Frends.domain.account.service.SignInService
import Rainbow_Frends.domain.account.service.impl.SignInServiceImpl
import gauth.*
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/login/gauth/code")
@RestController
class AuthController(private val signInService: SignInService) {
    @Value("\${GAuth-CLIENT-ID}")
    private val clientId: String? = null

    @Value("\${GAuth-CLIENT-SECRET}")
    private val clientSecret: String? = null

    @Value("\${GAuth-REDIRECT-URI}")
    private val redirectUri: String? = null
    @PostMapping
    fun signIn(@RequestBody @Valid signInRequest: SignInRequest): ResponseEntity<TokenResponse> {
        val response = signInService.execute(signInRequest)
        return ResponseEntity.ok(response)
    }

}