package Rainbow_Frends.global.config

import Rainbow_Frends.global.GAuth.JWT.CustomAuthenticationSuccessHandler
import Rainbow_Frends.global.GAuth.JWT.JwtAuthenticationFilter
import dev.yangsijun.gauth.configurer.GAuthLoginConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val gauth: GAuthLoginConfigurer<HttpSecurity>, private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        gauth.loginPageUrl("/gauth/authorization").successHandler(CustomAuthenticationSuccessHandler()).configure(http)

        http.csrf { it.disable() }.cors { it.disable() }.authorizeHttpRequests { request ->
            request.requestMatchers(
                "/gauth/authorization", "/swagger-ui/**", "/v3/api-docs/**", "/times/**"
            ).permitAll().requestMatchers("/role/student").hasAuthority("ROLE_STUDENT").requestMatchers("/role/teacher")
                .hasAuthority("ROLE_TEACHER").anyRequest().denyAll()
        }.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}