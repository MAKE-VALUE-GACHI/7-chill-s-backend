package gachi.chills.global.config

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.LocalDate
import java.time.LocalDateTime

@Configuration
class JacksonConfig {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
            .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN).apply {
                configOverride(LocalDate::class.java)
                    .setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd"))
                configOverride(LocalDateTime::class.java)
                    .setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
            }
    }
}
