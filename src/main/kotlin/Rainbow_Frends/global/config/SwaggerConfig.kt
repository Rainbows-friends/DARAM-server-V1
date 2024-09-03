package Rainbow_Frends.global.config

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun timeApi(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("Time").pathsToMatch("/api/times/**")
            .packagesToScan("Rainbow_Frends.domain.time.controller").build()
    }

    @Bean
    fun gauthApi(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("GAuth").pathsToMatch("/api/login/gauth/**")
            .packagesToScan("Rainbow_Frends.domain.auth.controller").build()
    }

    @Bean
    fun noticeApi(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("Notice").pathsToMatch("/api/notice/**")
            .packagesToScan("Rainbow_Frends.domain.notice.controller").build()
    }

    @Bean
    fun accountApi(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("Account").pathsToMatch("/api/account/**")
            .packagesToScan("Rainbow_Frends.domain.account.controller").build()
    }

    @Bean
    fun checkinApi(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("Checkin").pathsToMatch("/api/checkin/**")
            .packagesToScan("Rainbow_Frends.domain.checkin.controller").build()
    }
}