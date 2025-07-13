package gachi.chills.domain.post.service

import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.post.controller.request.PublishPostRequest
import gachi.chills.domain.post.controller.response.MyPostsResponse
import gachi.chills.domain.post.controller.response.PublishPostResponse
import gachi.chills.domain.post.domain.model.Post
import gachi.chills.domain.post.domain.model.PostTopic
import gachi.chills.domain.post.domain.repository.PostRepository
import gachi.chills.domain.post.domain.repository.PostTopicRepository
import gachi.chills.domain.post.exception.PostExceptionCode
import gachi.chills.domain.topic.domain.model.Topic
import gachi.chills.domain.topic.domain.repository.TopicRepository
import gachi.chills.domain.topic.exception.TopicExceptionCode
import gachi.chills.domain.user.domain.repository.UserRepository
import gachi.chills.domain.user.domain.repository.findByIdOrThrow
import gachi.chills.global.exception.BusinessException
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

    @Transactional(readOnly = true)
    fun getMyPosts(userContext: UserContext): List<MyPostsResponse> {
        val user = userRepository.findByIdOrThrow(userContext.id)

        val posts = postRepository
            .findAllByUserIdOrderByCreatedAtDesc(user.id)
            .associateBy { it.id }

        val postTopics = postTopicRepository.findAllByPostIdIn(posts.keys.toList())

        val topics = topicRepository
            .findAllById(postTopics.map { it.topicId })
            .groupBy { it.id }

        val postToTopicsMap: Map<Post, List<Topic>> = postTopics.associate { postTopic ->
            val post = posts[postTopic.postId] ?: throw BusinessException(PostExceptionCode.POST_NOT_FOUND)
            val topics = topics[postTopic.topicId] ?: throw BusinessException(TopicExceptionCode.TOPIC_NOT_FOUND)

            return@associate post to topics
        }

        return postToTopicsMap.map { (post, topics) ->
            MyPostsResponse.of(
                post = post,
                topics = topics,
            )
        }
    }
}
