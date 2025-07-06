package gachi.chills.infrastructure.auth.google

import gachi.chills.domain.auth.service.adapter.OAuthProcessor
import gachi.chills.domain.auth.service.adapter.OAuthResult
import gachi.chills.domain.auth.service.adapter.OAuthType
import gachi.chills.global.config.OAuthProperties
import gachi.chills.global.exception.BusinessException
import gachi.chills.global.exception.GlobalExceptionCode
import gachi.chills.infrastructure.auth.google.dto.request.GetGoogleOAuthTokenRequest
import gachi.chills.infrastructure.auth.google.dto.response.GetGoogleOAuthTokenResponse
import gachi.chills.infrastructure.auth.google.dto.response.GetGoogleOAuthUserInfoResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
internal class GoogleOAuthProcessor(
    private val restClient: RestClient,
    private val oAuthProperties: OAuthProperties,
) : OAuthProcessor {
    override fun supports(identifier: String): Boolean = identifier == OAuthType.GOOGLE.identifier

    override fun process(code: String): OAuthResult {
        val token = restClient.post()
            .uri(GOOGLE_OAUTH_GET_TOKEN_URL)
            .body(
                GetGoogleOAuthTokenRequest(
                    clientId = oAuthProperties.google.clientId,
                    clientSecret = oAuthProperties.google.clientSecret,
                    code = code,
                    redirectUri = oAuthProperties.google.redirectUri,
                )
            )
            .retrieve()
            .body<GetGoogleOAuthTokenResponse>()
            ?.accessToken
            ?: throw BusinessException(
                code = GlobalExceptionCode.UNEXPECTED_SERVER_ERROR,
                detail = "Failed to retrieve Google OAuth token",
            )

        val userInfo = restClient.get()
            .uri("$GOOGLE_OAUTH_GET_USER_INFO$token")
            .retrieve()
            .body<GetGoogleOAuthUserInfoResponse>()
            ?: throw BusinessException(
                code = GlobalExceptionCode.UNEXPECTED_SERVER_ERROR,
                detail = "Failed to retrieve Google OAuth user info",
            )

        return OAuthResult(
            name = userInfo.name,
            email = userInfo.email,
        )
    }

    companion object {
        const val GOOGLE_OAUTH_GET_TOKEN_URL = "https://oauth2.googleapis.com/token"
        const val GOOGLE_OAUTH_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token="
    }
}
