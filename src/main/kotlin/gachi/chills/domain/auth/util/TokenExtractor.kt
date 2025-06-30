package gachi.chills.domain.auth.util

import gachi.chills.domain.auth.exception.AuthExceptionCode
import gachi.chills.global.annotation.TokenType
import gachi.chills.global.exception.BusinessException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import javax.crypto.SecretKey

object TokenExtractor {
    private const val AUTHORIZATION_SEPARATOR = " "

    fun extractAccessTokenOrNull(request: HttpServletRequest): String? {
        val token: String? = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (token.isNullOrEmpty()) {
            return null
        }

        return checkToken(token.split(AUTHORIZATION_SEPARATOR))
    }

    fun extractAccessTokenOrThrow(request: HttpServletRequest): String {
        return extractAccessTokenOrNull(request)
            ?: throw BusinessException(AuthExceptionCode.AUTH_REQUIRED)
    }

    fun extractRefreshTokenOrNull(request: HttpServletRequest): String? {
        val cookies: Array<Cookie>? = request.cookies
        if (cookies.isNullOrEmpty()) {
            return null
        }
        return cookies.filter { it.name == TokenType.REFRESH_TOKEN_COOKIE }
            .map { it.value }
            .firstOrNull()
    }

    fun extractRefreshTokenOrThrow(request: HttpServletRequest): String {
        return extractRefreshTokenOrNull(request)
            ?: throw BusinessException(AuthExceptionCode.AUTH_REQUIRED)
    }

    fun extractClaims(
        token: String,
        secretKey: SecretKey,
    ): Jws<Claims> {
        return Jwts.parser()
            .requireIssuer(ISSUER)
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
    }

    private fun checkToken(parts: List<String>): String? {
        if (parts.size == 2 && parts[0] == TokenType.AUTHORIZATION_SCHEME) {
            return parts[1]
        }
        return null
    }
}
