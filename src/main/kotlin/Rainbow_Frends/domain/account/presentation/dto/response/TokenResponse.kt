package Rainbow_Frends.domain.account.presentation.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") val accessTokenExpiresIn: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") val refreshTokenExpiresIn: LocalDateTime
)