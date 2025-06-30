package gachi.chills.domain.auth.controller.response

data class ReissueTokenAuthResponse(
    val accessToken: String,
    val refreshToken: String,
)
