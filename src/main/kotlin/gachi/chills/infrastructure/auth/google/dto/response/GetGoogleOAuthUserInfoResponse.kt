package gachi.chills.infrastructure.auth.google.dto.response

data class GetGoogleOAuthUserInfoResponse(
    val email: String,
    val name: String,
)
