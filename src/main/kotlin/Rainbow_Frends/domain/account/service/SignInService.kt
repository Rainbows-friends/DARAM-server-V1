package Rainbow_Frends.domain.account.service

import Rainbow_Frends.domain.account.presentation.dto.request.SignInRequest
import Rainbow_Frends.domain.account.presentation.dto.response.TokenResponse

interface SignInService {
    fun execute(signInRequest: SignInRequest): TokenResponse
}