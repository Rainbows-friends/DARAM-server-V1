package Rainbow_Frends.global.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisUtil(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val redisBlackListTemplate: RedisTemplate<String, Any>
) {

    fun set(key: String, value: Any, minutes: Int) {
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(value.javaClass)
        redisTemplate.opsForValue().set(key, value, minutes.toLong(), TimeUnit.MINUTES)
    }

    fun get(key: String): Any? {
        return redisTemplate.opsForValue()[key]
    }

    fun delete(key: String): Boolean {
        return redisTemplate.delete(key)
    }

    fun hasKey(key: String): Boolean {
        return redisTemplate.hasKey(key) == true
    }

    fun setBlackList(key: String, value: Any, milliSeconds: Long) {
        redisBlackListTemplate.valueSerializer = Jackson2JsonRedisSerializer(value.javaClass)
        redisBlackListTemplate.opsForValue().set(key, value, milliSeconds, TimeUnit.MILLISECONDS)
    }

    fun getBlackList(key: String): Any? {
        return redisBlackListTemplate.opsForValue()[key]
    }

    fun deleteBlackList(key: String): Boolean {
        return redisBlackListTemplate.delete(key)
    }

    fun hasKeyBlackList(key: String): Boolean {
        return redisBlackListTemplate.hasKey(key) == true
    }
}