package gachi.chills.infrastructure.auth

import gachi.chills.domain.auth.service.support.TokenStore
import gachi.chills.global.config.TokenProperties
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

@Component
internal class RedisTokenStore(
    private val redisTemplate: StringRedisTemplate,
    private val tokenProperties: TokenProperties,
) : TokenStore {

    override fun synchronizeRefreshToken(
        identifier: String,
        refreshToken: String,
    ) {
        val key = generateKeyForStoreToken(identifier)
        redisTemplate.opsForValue().set(
            key,
            refreshToken,
            Duration.ofSeconds(tokenProperties.refreshTokenValidity),
        )
    }

    override fun deleteRefreshToken(identifier: String) {
        val key = generateKeyForStoreToken(identifier)
        redisTemplate.delete(key)
    }

    private fun generateKeyForStoreToken(identifier: String): String {
        return "user:$identifier:token"
    }
}
