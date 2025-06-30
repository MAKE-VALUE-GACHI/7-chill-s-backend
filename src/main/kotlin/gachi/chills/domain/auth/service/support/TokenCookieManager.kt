package gachi.chills.domain.auth.service.support

import gachi.chills.global.annotation.TokenType
import gachi.chills.global.config.TokenProperties
import gachi.chills.global.util.isNotNullOrBlank
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.web.server.Cookie
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class TokenCookieManager(
    private val tokenProperties: TokenProperties,
) {
    fun apply(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessToken: String,
        refreshToken: String,
    ) {
        setCookie(
            request = request,
            response = response,
            tokenType = TokenType.ACCESS_TOKEN_COOKIE,
            token = accessToken,
        )
        setCookie(
            request = request,
            response = response,
            tokenType = TokenType.REFRESH_TOKEN_COOKIE,
            token = accessToken,
        )
    }

    private fun setCookie(
        request: HttpServletRequest,
        response: HttpServletResponse,
        tokenType: String,
        token: String,
    ) {
        response.setHeader(
            HttpHeaders.SET_COOKIE,
            generateCookie(
                request = request,
                tokenType = tokenType,
                token = token,
            ),
        )
    }

    private fun generateCookie(
        request: HttpServletRequest,
        tokenType: String,
        token: String,
    ): String {
        return ResponseCookie
            .from(tokenType, token)
            .maxAge(tokenProperties.refreshTokenValidity)
            .sameSite(checkRequestOriginAndApplySameSite(request).attributeValue())
            .secure(isLocalRequest(request).not())
            .httpOnly(true)
            .path("/")
            .build()
            .toString()
    }

    fun expire(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        response.setHeader(
            HttpHeaders.SET_COOKIE,
            ResponseCookie
                .from(TokenType.REFRESH_TOKEN_COOKIE, "")
                .maxAge(1)
                .sameSite(Cookie.SameSite.LAX.attributeValue())
                .secure(isLocalRequest(request).not())
                .httpOnly(true)
                .path("/")
                .build()
                .toString(),
        )
    }

    private fun checkRequestOriginAndApplySameSite(request: HttpServletRequest): Cookie.SameSite {
        return if (isLocalRequest(request)) Cookie.SameSite.NONE else Cookie.SameSite.LAX
    }

    private fun isLocalRequest(request: HttpServletRequest): Boolean {
        val origin = request.getHeader(HttpHeaders.ORIGIN)
        return origin.isNotNullOrBlank() && origin in LOCALHOST_DOMAINS
    }

    companion object {
        private val LOCALHOST_DOMAINS = listOf("localhost", "127.0.0.1")
    }
}
