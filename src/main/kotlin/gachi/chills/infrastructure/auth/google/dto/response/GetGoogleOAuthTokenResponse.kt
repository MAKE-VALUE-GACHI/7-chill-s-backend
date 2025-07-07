package gachi.chills.infrastructure.auth.google.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GetGoogleOAuthTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
)
