package Rainbow_Frends.domain.auth.controller

import Rainbow_Frends.domain.account.presentation.dto.request.SignInRequest
import Rainbow_Frends.domain.account.presentation.dto.response.TokenResponse
import Rainbow_Frends.domain.account.service.LogoutService
import Rainbow_Frends.domain.account.service.ReissueTokenService
import Rainbow_Frends.domain.account.service.SignInService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.*

@Tag(name = "인증", description = "인증 관련 API")
@RequestMapping("/api/login/gauth")
@RestController
class AuthController(
    private val signInService: SignInService,
    private val logoutService: LogoutService,
    private val reissueTokenService: ReissueTokenService
) {

    @Operation(summary = "로그인", description = "GAuth 인증 코드를 사용하여 로그인합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = [Content(schema = Schema(implementation = TokenResponse::class))]
        ), ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    @PostMapping("/code")
    fun signIn(@RequestBody @Valid signInRequest: SignInRequest): ResponseEntity<TokenResponse> {
        val response = signInService.execute(signInRequest)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "토큰 재발급 성공",
            content = [Content(schema = Schema(implementation = TokenResponse::class))]
        ), ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    @PatchMapping("/reissue")
    fun reissueToken(@RequestHeader("Authorization") refreshToken: String): ResponseEntity<TokenResponse> {
        val tokenResponse: TokenResponse = reissueTokenService.execute(refreshToken)
        return ResponseEntity.ok(tokenResponse)
    }

    @Operation(summary = "로그아웃", description = "현재 로그인된 사용자를 로그아웃합니다.")
    @ApiResponses(
        value = [ApiResponse(responseCode = "204", description = "로그아웃 성공"), ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )]
    )
    @DeleteMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<Void> {
        logoutService.execute(request.getHeader("Authorization").substring(7))
        return ResponseEntity.noContent().build()
    }
}