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
