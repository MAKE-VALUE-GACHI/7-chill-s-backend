package gachi.chills.global.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(basePackages = ["gachi.chills.global.config"])
class ConfigurationPropertyConfig

@ConfigurationProperties(prefix = "springdoc.swagger-ui.classification")
data class SwaggerClassificationProperties(
    val readyToUseGroupName: String,
    val workInProgressGroupName: String,
    val deprecatedGroupName: String,
)

@ConfigurationProperties(prefix = "token")
data class TokenProperties(
    val secretKey: String,
    val accessTokenValidity: Long,
    val refreshTokenValidity: Long,
)

@ConfigurationProperties(prefix = "spring.data.redis")
data class RedisProperties(
    val host: String,
    val port: Int,
)

@ConfigurationProperties(prefix = "redirect")
data class RedirectProperties(
    val afterLogin: Url,
) {
    data class Url(
        val url: String,
    )
}
