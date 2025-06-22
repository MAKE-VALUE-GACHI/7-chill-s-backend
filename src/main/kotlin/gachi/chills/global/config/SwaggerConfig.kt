package gachi.chills.global.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@OpenAPIDefinition(
    info = Info(
        title = "Chill guys' Chill's API",
        description = "가치 7팀 (Chill guys') Chill's API 명세서",
        version = "v1",
    ),
)
@Configuration
class SwaggerConfig(
    private val swaggerClassificationProperties: SwaggerClassificationProperties,
) : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addRedirectViewController("/", "/swagger-ui.html")
    }

    @Bean
    fun readyToUse(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group(swaggerClassificationProperties.readyToUseGroupName)
            .pathsToMatch("")
            .build()
    }

    @Bean
    fun workInProgress(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group(swaggerClassificationProperties.workInProgressGroupName)
            .pathsToMatch("/**")
            .build()
    }

    @Bean
    fun deprecated(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group(swaggerClassificationProperties.deprecatedGroupName)
            .pathsToMatch("")
            .build()
    }

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .addSecurityItem(createSecurityItem())
            .components(createComponents())
    }

    private fun createSecurityItem(): SecurityRequirement? {
        return SecurityRequirement()
            .addList(WITH_ACCESS_TOKEN)
            .addList(WITH_REFRESH_TOKEN)
    }

    private fun createComponents(): Components {
        return Components()
            .addSecuritySchemes(
                WITH_ACCESS_TOKEN,
                SecurityScheme()
                    .name(WITH_ACCESS_TOKEN)
                    .type(SecurityScheme.Type.HTTP)
                    .`in`(SecurityScheme.In.HEADER)
                    .scheme("Bearer")
                    .bearerFormat("JWT")
            ).addSecuritySchemes(
                WITH_REFRESH_TOKEN,
                SecurityScheme()
                    .name(WITH_REFRESH_TOKEN)
                    .type(SecurityScheme.Type.APIKEY)
                    .`in`(SecurityScheme.In.HEADER)
            )
    }

    companion object {
        private const val WITH_ACCESS_TOKEN = "Authorization"
        private const val WITH_REFRESH_TOKEN = "refresh-token"
    }
}
