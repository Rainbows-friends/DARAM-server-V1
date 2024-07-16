package Rainbow_Frends.global

import dev.yangsijun.gauth.configurer.GAuthLoginConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(private val gauth: GAuthLoginConfigurer<HttpSecurity>) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }.cors { cors -> cors.disable() }.authorizeHttpRequests { request ->
                request.requestMatchers(
                    "/gauth/authorization", "/swagger-ui/**", "/v3/api-docs/**"
                ).permitAll().anyRequest().authenticated()
            }

        gauth.configure(http)

        return http.build()
    }
}