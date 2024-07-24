package Rainbow_Frends.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }.cors { it.disable() }.authorizeHttpRequests { request ->
                request.requestMatchers(
                    "/swagger-ui/**", "/v3/api-docs/**"
                ).permitAll().anyRequest().denyAll()
            }

        return http.build()
    }
}