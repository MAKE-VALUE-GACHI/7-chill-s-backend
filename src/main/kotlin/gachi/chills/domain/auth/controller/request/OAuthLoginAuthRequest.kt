package gachi.chills.domain.auth.controller.request

data class OAuthLoginAuthRequest(
    val identifier: String,
    val code: String,
)
