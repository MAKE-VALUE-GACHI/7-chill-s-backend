package gachi.chills.domain.post.controller.request

import gachi.chills.domain.post.domain.model.Post
import gachi.chills.domain.user.domain.model.User
import io.swagger.v3.oas.annotations.media.Schema

data class PublishPostRequest(
    @field:Schema(description = "제목", required = true)
    val title: String,

    @field:Schema(description = "내용", required = true)
    val content: String,

    @field:Schema(description = "공개 여부", defaultValue = "true")
    val isPublic: Boolean = true,

    @field:Schema(description = "토픽 ID 목록")
    val topicIds: List<Long> = emptyList(),
) {
    fun toEntity(user: User): Post {
        return Post(
            user = user,
            title = title,
            content = content,
            isPublic = isPublic,
        )
    }
}
