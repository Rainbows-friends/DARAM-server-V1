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
                        "/gauth/authorization", "/login/gauth/code"
                    ).permitAll().requestMatchers("/auth/me").authenticated().requestMatchers("/role/student")
                    .hasAuthority("GAUTH_ROLE_STUDENT").requestMatchers("/role/teacher")
                    .hasAuthority("GAUTH_ROLE_TEACHER").anyRequest().denyAll()
            }.logout { logout -> logout }

        gauth.configure(http)

        return http.build()
    }
}