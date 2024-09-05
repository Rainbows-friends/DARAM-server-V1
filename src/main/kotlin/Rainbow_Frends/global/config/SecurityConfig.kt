package Rainbow_Frends.global.config

import Rainbow_Frends.global.security.filter.DevFilter
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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val gAuthLoginConfigurer: GAuthLoginConfigurer<HttpSecurity>,
    private val jwtProvider: JwtProvider,
    private val devFilter: DevFilter
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }.cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { request ->
                request.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/health").permitAll()
                    .requestMatchers(
                        "/gauth/authorization",
                        "/api/login/gauth/code",
                        "/api/login/gauth/logout",
                        "/api/login/gauth/reissue"
                    ).permitAll().requestMatchers(
                        "/auth/me",
                        "/api/times/remaintime",
                        "/api/notice",
                        "/api/notice/{id}",
                        "/api/notice/all",
                        "/api/account",
                        "/api/account/profile-picture",
                        "/api/checkin",
                        "/api/checkin/unchecked"
                    ).authenticated().requestMatchers("/role/student").hasAuthority("GAUTH_ROLE_STUDENT")
                    .requestMatchers("/role/teacher").hasAuthority("GAUTH_ROLE_TEACHER").anyRequest().denyAll()
            }.addFilterBefore(devFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter::class.java)

        gAuthLoginConfigurer.configure(http)
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val unrestrictedCorsConfig = CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("Authorization", "Content-Type")
            allowCredentials = false
        }

        val restrictedCorsConfig = CorsConfiguration().apply {
            allowedOrigins = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("Authorization", "Content-Type")
            allowCredentials = false
            exposedHeaders = listOf("Authorization")
        }

        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/swagger-ui/**", unrestrictedCorsConfig)
            registerCorsConfiguration("/v3/api-docs/**", unrestrictedCorsConfig)

            registerCorsConfiguration("/gauth/authorization", restrictedCorsConfig)
            registerCorsConfiguration("/api/login/gauth/code", restrictedCorsConfig)
            registerCorsConfiguration("/api/login/gauth/logout", restrictedCorsConfig)
            registerCorsConfiguration("/api/login/gauth/reissue", restrictedCorsConfig)
            registerCorsConfiguration("/auth/me", restrictedCorsConfig)
            registerCorsConfiguration("/api/times/remaintime", restrictedCorsConfig)
            registerCorsConfiguration("/api/notice", restrictedCorsConfig)
            registerCorsConfiguration("/api/notice/all", restrictedCorsConfig)
            registerCorsConfiguration("/api/account/**", restrictedCorsConfig)
        }
        return source
    }
}