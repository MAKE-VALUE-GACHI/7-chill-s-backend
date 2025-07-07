package gachi.chills.domain.auth.service.adapter.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy::class)
data class GoogleUserResponse(
	val sub: String,
	val name: String,
	val email: String,
	val emailVerified: Boolean,
)
