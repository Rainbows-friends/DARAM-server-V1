package Rainbow_Frends.global.gauth

import gauth.GAuth
import gauth.impl.GAuthImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GAuthBean {
    @Bean
    fun gauth(): GAuth {
        return GAuthImpl()
    }
}