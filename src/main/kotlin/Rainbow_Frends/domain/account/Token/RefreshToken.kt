package Rainbow_Frends.domain.account.Token

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.time.LocalDateTime

@RedisHash(value = "RefreshToken", timeToLive = 60)
class RefreshToken {
    @Id
    private var id: String? = null

    @Indexed
    private var UUID: String? = null
    private var expired: LocalDateTime? = null
}