package Rainbow_Frends.global.config

import org.ehcache.event.CacheEvent
import org.ehcache.event.CacheEventListener
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.cache.CacheManager
import javax.cache.Caching

@Configuration
@EnableCaching
class CacheConfig : CacheEventListener<Any, Any> {
    private val log = LoggerFactory.getLogger(CacheConfig::class.java)
    override fun onEvent(cacheEvent: CacheEvent<out Any, out Any>) {
        log.info("Key: [${cacheEvent.key}] | EventType: [${cacheEvent.type}] | Old value: [${cacheEvent.oldValue}] | New value: [${cacheEvent.newValue}]")
    }

    @Bean
    fun cacheManager(): CacheManager? {
        val ehcacheConfig = this::class.java.getResource("classpath:ehcache.xml")
        val cachingProvider = Caching.getCachingProvider()
        log.info("EHCache config file location: $ehcacheConfig")
        if (ehcacheConfig != null) {
            return cachingProvider.getCacheManager(ehcacheConfig.toURI(), cachingProvider.defaultClassLoader)
        }
        log.error("EHCache config file not found!")
        return null
    }
}