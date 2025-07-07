package gachi.chills.domain.user.controller.response

import gachi.chills.domain.topic.domain.model.Topic
import gachi.chills.domain.user.domain.model.User
import io.swagger.v3.oas.annotations.media.Schema

data class GetMyProfileResponse(
    @field:Schema(description = "사용자 ID", example = "ccaa20b0e4764662b205eab16a2c3f91", required = true)
    val id: String,

    @field:Schema(description = "사용자 이름", example = "홍길동", required = true)
    val name: String,

    @field:Schema(description = "사용자 이메일", example = "example@example.com", required = true)
    val email: String,
    val topics: List<MyTopicResponse>,
) {
    data class MyTopicResponse(
        @field:Schema(description = "토픽 ID", example = "1", required = true)
        val id: Long,

        @field:Schema(description = "사용자 ID", example = "여행", required = true)
        val title: String,
    ) {
        companion object {
            fun from(topic: Topic): MyTopicResponse {
                return MyTopicResponse(
                    id = topic.id,
                    title = topic.title,
                )
            }
        }
    }

    companion object {
        fun of(
            user: User,
            topics: List<Topic>,
        ): GetMyProfileResponse {
            return GetMyProfileResponse(
                id = user.id,
                name = user.name,
                email = user.email,
                topics = topics.map { MyTopicResponse.from(it) },
            )
        }
    }
}
