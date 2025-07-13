package gachi.chills.domain.post.service

import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.post.controller.request.PublishPostRequest
import gachi.chills.domain.post.controller.response.PublishPostResponse
import gachi.chills.domain.post.domain.model.PostTopic
import gachi.chills.domain.post.domain.repository.PostRepository
import gachi.chills.domain.post.domain.repository.PostTopicRepository
import gachi.chills.domain.topic.domain.repository.TopicRepository
import gachi.chills.domain.user.domain.repository.UserRepository
import gachi.chills.domain.user.domain.repository.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val topicRepository: TopicRepository,
    private val postTopicRepository: PostTopicRepository,
) {
    @Transactional
    fun publish(
        userContext: UserContext,
        request: PublishPostRequest,
    ): PublishPostResponse {
        val user = userRepository.findByIdOrThrow(userContext.id)

        val post = postRepository.save(request.toEntity(user))
        val topics = topicRepository
            .findAllById(request.topicIds)
            .also { topics ->
                check(request.topicIds.size == topics.size) {
                    "잘못된 토픽 ID가 포함되어 있습니다."
                }

                val postTopics = topics.map { topic ->
                    PostTopic(
                        post = post,
                        topic = topic,
                    )
                }
                postTopicRepository.saveAll(postTopics)
            }

        return PublishPostResponse.of(
            post = post,
            topics = topics,
        )
    }
}
