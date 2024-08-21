package Rainbow_Frends.global.config

import dev.yangsijun.gauth.configurer.GAuthLoginConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val gAuthLoginConfigurer: GAuthLoginConfigurer<HttpSecurity>
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }.cors { it.disable() }.authorizeHttpRequests { request ->
            request.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/gauth/authorization", "/login/gauth/code").permitAll().requestMatchers("/auth/me")
                .authenticated().requestMatchers("/role/student").hasAuthority("GAUTH_ROLE_STUDENT")
                .requestMatchers("/role/teacher").hasAuthority("GAUTH_ROLE_TEACHER").anyRequest().denyAll()

        }.logout { it.logoutRequestMatcher(AntPathRequestMatcher("/logout")).permitAll() }
        gAuthLoginConfigurer.configure(http)
        return http.build()
    }
}