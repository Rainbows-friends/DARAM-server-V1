package Rainbow_Frends.global.config

import Rainbow_Frends.global.security.filter.JwtFilter
import Rainbow_Frends.global.security.jwt.JwtProvider
import dev.yangsijun.gauth.configurer.GAuthLoginConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val gAuthLoginConfigurer: GAuthLoginConfigurer<HttpSecurity>, private val jwtProvider: JwtProvider
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }.cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { request ->
                request.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll().requestMatchers(
                        "/gauth/authorization",
                        "/login/gauth/code",
                        "login/gauth/logout",
                        "login/gauth/reissue"
                    ).permitAll().requestMatchers("/auth/me", "times/remaintime").authenticated()
                    .requestMatchers("/role/student").hasAuthority("GAUTH_ROLE_STUDENT")
                    .requestMatchers("/role/teacher").hasAuthority("GAUTH_ROLE_TEACHER").anyRequest().denyAll()
            }.addFilterBefore(JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter::class.java)

        gAuthLoginConfigurer.configure(http)
        return http.build()
    }
}