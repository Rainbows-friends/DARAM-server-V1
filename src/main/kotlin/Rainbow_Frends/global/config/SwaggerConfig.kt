package Rainbow_Frends.global.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import io.swagger.v3.oas.models.tags.Tag
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

    @Bean
    fun customOpenAPI(): OpenAPI {
        val gauthPathItem = PathItem().get(
            io.swagger.v3.oas.models.Operation().addTagsItem("인증").summary("로그인 페이지").addParametersItem(
                    Parameter().`in`("query").name("param").description("GAuth 로그인 페이지를 반환합니다")
                ).responses(
                    ApiResponses().addApiResponse(
                        "200", ApiResponse().description("Successful GAuth authorization")
                    )
                )
        )

        return OpenAPI().info(
            Info().title("Rainbow Frends API").version("v1.0")
        ).paths(
            Paths().addPathItem("/gauth/authorization", gauthPathItem)
        ).addTagsItem(
            Tag().name("인증").description("인증 관련 API")
        )
    }
}