package Rainbow_Frends.domain.account.Token

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime

@RedisHash(value = "RefreshToken", timeToLive = 60)
data class RefreshToken(
    @Id val refreshToken: String, @Indexed val UserId: String, val expiredAt: LocalDateTime
)