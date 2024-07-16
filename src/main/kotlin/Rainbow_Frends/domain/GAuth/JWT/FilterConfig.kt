package Rainbow_Frends.domain.GAuth.JWT

import Rainbow_Frends.domain.GAuth.GAuthRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig(private val gauthRepository: GAuthRepository) {

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(gauthRepository)
    }
}