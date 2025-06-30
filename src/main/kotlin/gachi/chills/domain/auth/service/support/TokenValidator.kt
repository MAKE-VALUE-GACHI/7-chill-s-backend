package gachi.chills.domain.auth.service.support

import gachi.chills.domain.auth.exception.AuthExceptionCode
import gachi.chills.domain.auth.util.ACCESS_TOKEN_SUBJECT
import gachi.chills.domain.auth.util.REFRESH_TOKEN_SUBJECT
import gachi.chills.domain.auth.util.TokenExtractor
import gachi.chills.global.config.TokenProperties
import gachi.chills.global.exception.BusinessException
import gachi.chills.global.util.toSecretKey
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.stereotype.Component

@Component
class TokenValidator(
    private val tokenProperties: TokenProperties,
) {
    fun validateAccessToken(token: String) = validateToken(token, ACCESS_TOKEN_SUBJECT)

    fun validateRefreshToken(token: String) = validateToken(token, REFRESH_TOKEN_SUBJECT)

    private fun validateToken(
        token: String,
        subject: String,
    ) {
        runCatching {
            val claims = TokenExtractor.extractClaims(
                token = token,
                secretKey = tokenProperties.secretKey.toSecretKey()
            )
            val payload = claims.payload
            checkSubject(payload, subject)
        }.onFailure {
            when (it) {
                is BusinessException -> throw it

                is ExpiredJwtException -> throw BusinessException(
                    code = AuthExceptionCode.AUTH_REQUIRED,
                    detail = "Token expired",
                )

                else -> throw BusinessException(
                    code = AuthExceptionCode.AUTH_REQUIRED,
                    detail = "Invalid token",
                )
            }
        }
    }

    private fun checkSubject(
        payload: Claims,
        subject: String,
    ) {
        if (subject != payload.subject) {
            throw BusinessException(
                code = AuthExceptionCode.AUTH_REQUIRED,
                detail = "Invalid token subject",
            )
        }
    }
}
