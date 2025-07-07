package gachi.chills.domain.auth.service.support

import gachi.chills.domain.auth.util.ACCESS_TOKEN_SUBJECT
import gachi.chills.domain.auth.util.IDENTIFIER
import gachi.chills.domain.auth.util.ISSUER
import gachi.chills.domain.auth.util.JWT
import gachi.chills.domain.auth.util.REFRESH_TOKEN_SUBJECT
import gachi.chills.domain.auth.util.TYPE
import gachi.chills.domain.auth.util.TokenExtractor
import gachi.chills.global.config.TokenProperties
import gachi.chills.global.util.toSecretKey
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.util.*

@Component
class TokenIssuer(
    private val tokenProperties: TokenProperties,
    private val tokenStore: TokenStore,
) {
    /**
     * @return AccessToken to RefreshToken
     */
    fun issueToken(identifier: String): Pair<String, String> {
        val accessToken = createAccessToken(identifier)
        val refreshToken = createRefreshToken(identifier)

        tokenStore.synchronizeRefreshToken(identifier, refreshToken)

        return accessToken to refreshToken
    }

    fun getIdentifier(token: String): String {
        return TokenExtractor.extractClaims(
            token = token,
            secretKey = tokenProperties.secretKey.toSecretKey()
        ).payload[IDENTIFIER].toString()
    }

    fun deleteRefreshToken(identifier: String) = tokenStore.deleteRefreshToken(identifier)

    private fun createAccessToken(identifier: String): String {
        val claims: Claims = Jwts.claims()
            .add(IDENTIFIER, identifier)
            .build()

        val now: ZonedDateTime = ZonedDateTime.now()
        val tokenValidity: ZonedDateTime = now.plusSeconds(tokenProperties.accessTokenValidity)

        return createToken(
            subject = ACCESS_TOKEN_SUBJECT,
            claims = claims,
            issuedAt = Date.from(now.toInstant()),
            expiration = Date.from(tokenValidity.toInstant()),
        )
    }

    private fun createRefreshToken(identifier: String): String {
        val claims: Claims = Jwts.claims()
            .add(IDENTIFIER, identifier)
            .build()

        val now: ZonedDateTime = ZonedDateTime.now()
        val tokenValidity: ZonedDateTime = now.plusSeconds(tokenProperties.refreshTokenValidity)

        return createToken(
            subject = REFRESH_TOKEN_SUBJECT,
            claims = claims,
            issuedAt = Date.from(now.toInstant()),
            expiration = Date.from(tokenValidity.toInstant()),
        )
    }

    private fun createToken(
        subject: String,
        claims: Claims,
        issuedAt: Date,
        expiration: Date,
    ): String {
        return Jwts.builder()
            .id(UUID.randomUUID().toString())
            .header()
            .add(TYPE, JWT)
            .and()
            .claims(claims)
            .subject(subject)
            .issuer(ISSUER)
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(tokenProperties.secretKey.toSecretKey())
            .compact()
    }
}
