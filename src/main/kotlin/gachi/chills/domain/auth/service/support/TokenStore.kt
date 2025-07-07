package gachi.chills.domain.auth.service.support

interface TokenStore {
    fun synchronizeRefreshToken(
        identifier: String,
        refreshToken: String,
    )

    fun deleteRefreshToken(identifier: String)
}
