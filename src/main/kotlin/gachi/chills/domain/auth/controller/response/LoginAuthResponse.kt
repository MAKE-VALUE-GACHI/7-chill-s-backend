package gachi.chills.domain.auth.controller.response

data class LoginAuthResponse(
    val accessToken: String,
    val refreshToken: String,
)
