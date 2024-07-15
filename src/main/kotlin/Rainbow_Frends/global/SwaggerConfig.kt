package Rainbow_Frends.config

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun timeApi(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("times").pathsToMatch("/times/**")
            .packagesToScan("Rainbow_Frends.domain.times").build()
    }

    @Bean
    fun gauthApi(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("GAuth").pathsToMatch("/user/**")
            .packagesToScan("Rainbow_Frends.domain.GAuth").build()
    }
}