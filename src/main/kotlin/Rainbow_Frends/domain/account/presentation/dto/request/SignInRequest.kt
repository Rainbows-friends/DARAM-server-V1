package Rainbow_Frends.domain.account.presentation.dto.request

import jakarta.validation.constraints.NotBlank

class SignInRequest {
    @field:NotBlank
    val code: String? = null
}