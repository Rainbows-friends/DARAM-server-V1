package Rainbow_Frends.global.GAuth.config

import gauth.GAuth
import gauth.impl.GAuthImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GAuthConfig {
    @Bean
    fun gauth(): GAuth {
        return GAuthImpl()
    }

    inner class Component(private val gAuth: GAuth)
}