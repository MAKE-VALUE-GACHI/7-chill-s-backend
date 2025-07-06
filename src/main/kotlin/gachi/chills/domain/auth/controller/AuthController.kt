package gachi.chills.domain.auth.controller

import gachi.chills.domain.auth.controller.request.OAuthLoginAuthRequest
import gachi.chills.domain.auth.controller.response.ReissueTokenAuthResponse
import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.auth.service.AuthService
import gachi.chills.domain.auth.service.support.TokenCookieManager
import gachi.chills.global.annotation.Auth
import gachi.chills.global.annotation.ExtractToken
import gachi.chills.global.annotation.TokenType
import gachi.chills.global.config.RedirectProperties
import gachi.chills.global.util.redirect
import gachi.chills.global.util.toURI
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authService: AuthService,
    private val tokenCookieManager: TokenCookieManager,
    private val redirectProperties: RedirectProperties,
) : AuthControllerDocs {
    @Hidden
    @GetMapping("/login/oauth")
    fun login(
        @ModelAttribute request: OAuthLoginAuthRequest,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse,
    ): ResponseEntity<Unit> {
        val result = authService.login(request)
        tokenCookieManager.apply(
            request = httpRequest,
            response = httpResponse,
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
        )
        return redirect(redirectProperties.afterLogin.url.toURI())
    }

    @PostMapping("/reissue")
    override fun reissueToken(
        @ExtractToken(TokenType.REFRESH) refreshToken: String,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse,
    ): ResponseEntity<ReissueTokenAuthResponse> {
        val result = authService.reissueToken(refreshToken)
        tokenCookieManager.apply(
            request = httpRequest,
            response = httpResponse,
            accessToken = result.accessToken,
            refreshToken = result.refreshToken,
        )
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/logout")
    override fun logout(
        @Auth userContext: UserContext,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse,
    ): ResponseEntity<Unit> {
        authService.logout(userContext)
        tokenCookieManager.expire(httpRequest, httpResponse)
        return ResponseEntity.noContent().build()
    }
}

@Tag(
    name = "인증 API",
    description = "인증과 관련된 API 입니다.",
)
internal interface AuthControllerDocs {
    @Operation(
        summary = "토큰 갱신",
        description = "- 토큰을 갱신합니다.<br>- 세션에 저장된 Refresh Token 도 함께 갱신합니다.",
    )
    fun reissueToken(
        @ExtractToken(TokenType.REFRESH) refreshToken: String,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse,
    ): ResponseEntity<ReissueTokenAuthResponse>

    @Operation(
        summary = "로그아웃",
        description = "세션에 저장된 Refresh Token 을 제거합니다.",
    )
    fun logout(
        @Auth userContext: UserContext,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse,
    ): ResponseEntity<Unit>
}
