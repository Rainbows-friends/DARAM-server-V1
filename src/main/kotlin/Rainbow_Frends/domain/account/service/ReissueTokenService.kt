package Rainbow_Frends.domain.account.service

import Rainbow_Frends.domain.account.presentation.dto.response.TokenResponse

interface ReissueTokenService {
    fun execute(refreshToken: String): TokenResponse
}