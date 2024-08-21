package Rainbow_Frends.domain.account.Token

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime
import java.util.UUID

@RedisHash(value = "RefreshToken", timeToLive = 60)
data class RefreshToken(
    @Id val refreshToken: String, @Indexed val UserId: UUID, val expiredAt: LocalDateTime
)