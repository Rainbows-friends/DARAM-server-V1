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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val gAuthLoginConfigurer: GAuthLoginConfigurer<HttpSecurity>, private val jwtProvider: JwtProvider
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { request ->
                request.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers(
                        "/gauth/authorization",
                        "/api/login/gauth/code",
                        "/api/login/gauth/logout",
                        "/api/login/gauth/reissue"
                    ).permitAll()
                    .requestMatchers(
                        "/auth/me",
                        "/api/times/remaintime",
                        "/api/notice",
                        "/api/notice/all",
                        "/api/account/**"
                    ).authenticated()
                    .requestMatchers("/role/student").hasAuthority("GAUTH_ROLE_STUDENT")
                    .requestMatchers("/role/teacher").hasAuthority("GAUTH_ROLE_TEACHER")
                    .anyRequest().denyAll()
            }
            .addFilterBefore(JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter::class.java)

        gAuthLoginConfigurer.configure(http)
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val unrestrictedCorsConfig = CorsConfiguration()
        unrestrictedCorsConfig.allowedOrigins = listOf("*")
        unrestrictedCorsConfig.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        unrestrictedCorsConfig.allowedHeaders = listOf("Authorization", "Content-Type")
        unrestrictedCorsConfig.allowCredentials = false

        val restrictedCorsConfig = CorsConfiguration()
        restrictedCorsConfig.allowedOrigins = listOf("*")
        restrictedCorsConfig.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        restrictedCorsConfig.allowedHeaders = listOf("Authorization", "Content-Type")
        restrictedCorsConfig.allowCredentials = false
        restrictedCorsConfig.exposedHeaders = listOf("Authorization")

        val source = UrlBasedCorsConfigurationSource()

        source.registerCorsConfiguration("/swagger-ui/**", unrestrictedCorsConfig)
        source.registerCorsConfiguration("/v3/api-docs/**", unrestrictedCorsConfig)

        source.registerCorsConfiguration("/gauth/authorization", restrictedCorsConfig)
        source.registerCorsConfiguration("/api/login/gauth/code", restrictedCorsConfig)
        source.registerCorsConfiguration("/api/login/gauth/logout", restrictedCorsConfig)
        source.registerCorsConfiguration("/api/login/gauth/reissue", restrictedCorsConfig)
        source.registerCorsConfiguration("/auth/me", restrictedCorsConfig)
        source.registerCorsConfiguration("/api/times/remaintime", restrictedCorsConfig)
        source.registerCorsConfiguration("/api/notice", restrictedCorsConfig)
        source.registerCorsConfiguration("/api/notice/all", restrictedCorsConfig)
        source.registerCorsConfiguration("/api/account/**", restrictedCorsConfig)
        return source
    }
}