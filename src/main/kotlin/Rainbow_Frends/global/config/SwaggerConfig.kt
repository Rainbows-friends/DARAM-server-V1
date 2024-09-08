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
    fun gauthApi(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("GAuth").pathsToMatch("/api/login/gauth/**", "/gauth/authorization")
            .packagesToScan("Rainbow_Frends.domain.auth.controller").build()
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        val gauthAuthorizationPath = PathItem().get(
            io.swagger.v3.oas.models.Operation().addTagsItem("인증").summary("GAuth 로그인 페이지")
                .description("외부 GAuth 라이브러리에서 제공하는 인증 경로").addParametersItem(
                    Parameter().`in`("header").name("Authorization")
                        .description("DevKey: 임시 개발자 키를 입력하세요 (예: 7470c985ca283e19082b9ad5f875931e)").required(true)
                        .schema(io.swagger.v3.oas.models.media.StringSchema())
                ).addParametersItem(
                    Parameter().`in`("query").name("email").description("각자 이메일 (최소 1회 GAuth 로그인 필요)").required(true)
                        .schema(io.swagger.v3.oas.models.media.StringSchema())
                ).responses(
                    ApiResponses().addApiResponse(
                        "200", ApiResponse().description("GAuth 인증 성공")
                    ).addApiResponse(
                        "400", ApiResponse().description("잘못된 요청")
                    )
                )
        )

        return OpenAPI().info(
            Info().title("Rainbow Frends API").version("v1.0")
        ).paths(
            Paths().addPathItem("/gauth/authorization", gauthAuthorizationPath)
        ).addTagsItem(
            Tag().name("인증").description("인증 관련 API")
        )
    }
}