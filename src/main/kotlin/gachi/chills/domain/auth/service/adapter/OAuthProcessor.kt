package gachi.chills.domain.auth.service.adapter

interface OAuthProcessor {
    fun supports(identifier: String): Boolean

    fun process(code: String): SocialAuthVerifyResult
}

enum class OAuthType(
    val identifier: String,
) {
    GOOGLE("google"),
    ;
}

data class SocialAuthVerifyResult(
    val identifier: String,
    val name: String,
    val email: String,
    val emailVerified: Boolean,
    /**
     * Token
     */
    val idToken: String?,
    val accessToken: String,
    val refreshToken: String?,
)
