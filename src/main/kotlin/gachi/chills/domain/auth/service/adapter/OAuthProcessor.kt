package gachi.chills.domain.auth.service.adapter

interface OAuthProcessor {
    fun supports(identifier: String): Boolean

    fun process(code: String): OAuthResult
}

enum class OAuthType(
    val identifier: String,
) {
    GOOGLE("google"),
    ;
}

data class OAuthResult(
    val name: String,
    val email: String,
)
