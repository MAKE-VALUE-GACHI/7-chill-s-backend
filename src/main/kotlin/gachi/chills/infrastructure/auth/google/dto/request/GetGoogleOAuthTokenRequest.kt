package gachi.chills.infrastructure.auth.google.dto.request

data class GetGoogleOAuthTokenRequest(
    val clientId: String,
    val clientSecret: String,
    val code: String,
    val grantType: String = "authorization_code",
    val redirectUri: String,
)
