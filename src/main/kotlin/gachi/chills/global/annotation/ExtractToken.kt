package gachi.chills.global.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExtractToken(val type: TokenType)

enum class TokenType {
    ACCESS,
    REFRESH,
    ;

    companion object {
        const val AUTHORIZATION_SCHEME = "Bearer"
        const val ACCESS_TOKEN_COOKIE = "access-token"
        const val REFRESH_TOKEN_COOKIE = "refresh-token"
    }
}
