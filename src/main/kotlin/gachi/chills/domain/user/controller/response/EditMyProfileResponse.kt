package gachi.chills.domain.user.controller.response

import io.swagger.v3.oas.annotations.media.Schema

data class EditMyProfileResponse(
    @field:Schema(description = "사용자 이름", example = "홍길동", required = true)
    val name: String,

    @field:Schema(description = "사용자 관심 분야 ID 목록", example = "[1, 2, 3]", required = true)
    val topicIds: List<Long>,
)
