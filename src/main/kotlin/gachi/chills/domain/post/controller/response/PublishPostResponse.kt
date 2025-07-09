package gachi.chills.domain.post.controller.response

import gachi.chills.domain.post.domain.model.Post
import gachi.chills.domain.topic.domain.model.Topic
import io.swagger.v3.oas.annotations.media.Schema

data class PublishPostResponse(
    @field:Schema(description = "게시물 ID", required = true)
    val id: Long,

    @field:Schema(description = "제목", required = true)
    val title: String,

    @field:Schema(description = "내용", required = true)
    val content: String,

    @field:Schema(description = "요약", required = true)
    val description: String,

    @field:Schema(description = "공개 여부", defaultValue = "true")
    val isPublic: Boolean = true,
    val topics: List<TopicResponse> = emptyList(),
) {
    data class TopicResponse(
        @field:Schema(description = "토픽 ID", example = "1", required = true)
        val id: Long,

        @field:Schema(description = "사용자 ID", example = "여행", required = true)
        val title: String,
    ) {
        companion object {
            fun from(topic: Topic): TopicResponse {
                return TopicResponse(
                    id = topic.id,
                    title = topic.title,
                )
            }
        }
    }

    companion object {
        fun of(
            post: Post,
            topics: List<Topic>,
        ): PublishPostResponse {
            return PublishPostResponse(
                id = post.id,
                title = post.title,
                content = post.content,
                isPublic = post.isPublic,
                description = post.description,
                topics = topics.map { TopicResponse.from(it) },
            )
        }
    }
}
