package Rainbow_Frends.global.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}") private val redisHost: String,
    @Value("\${spring.data.redis.port}") private val redisPort: Int
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(redisHost, redisPort)
    }
    /*
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {

        return LettuceConnectionFactory(redisHost, redisPort)
    }
    */

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.connectionFactory = redisConnectionFactory()
        val serializer = GenericJackson2JsonRedisSerializer()

        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = serializer
        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.hashValueSerializer = serializer

        return redisTemplate
    }


    @Component
    class RedisUtil(
        private val redisTemplate: RedisTemplate<String, Any>,
        private val redisBlackListTemplate: RedisTemplate<String, Any>
    ) {

        fun set(key: String, value: Any, minutes: Int) {
            redisTemplate.opsForValue().set(key, value, minutes.toLong(), TimeUnit.MINUTES)
        }

        fun get(key: String): Any? {
            return redisTemplate.opsForValue().get(key)
        }

        fun delete(key: String): Boolean {
            return redisTemplate.delete(key)
        }

        fun hasKey(key: String): Boolean {
            return redisTemplate.hasKey(key) == true
        }

        fun setBlackList(key: String, value: Any, milliSeconds: Long) {
            redisBlackListTemplate.opsForValue().set(key, value, milliSeconds, TimeUnit.MILLISECONDS)
        }

        fun getBlackList(key: String): Any? {
            return redisBlackListTemplate.opsForValue().get(key)
        }

        fun deleteBlackList(key: String): Boolean {
            return redisBlackListTemplate.delete(key)
        }

        fun hasKeyBlackList(key: String): Boolean {
            return redisBlackListTemplate.hasKey(key) == true
        }
    }
}