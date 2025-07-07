package gachi.chills.domain.user.controller.response

import gachi.chills.domain.user.domain.model.User
import io.swagger.v3.oas.annotations.media.Schema

data class GetMyProfileResponse(
    @field:Schema(description = "사용자 ID", example = "ccaa20b0e4764662b205eab16a2c3f91", required = true)
    val id: String,

    @field:Schema(description = "사용자 이름", example = "홍길동", required = true)
    val name: String,

    @field:Schema(description = "사용자 이메일", example = "example@example.com", required = true)
    val email: String,

    @field:Schema(description = "사용자 관심 분야 ID 목록", example = "[1, 2, 3]", required = true)
    val topicIds: List<Long>,
) {
    companion object {
        fun of(
            user: User,
            topicIds: List<Long>,
        ): GetMyProfileResponse {
            return GetMyProfileResponse(
                id = user.id,
                name = user.name,
                email = user.email,
                topicIds = topicIds,
            )
        }
    }
}
