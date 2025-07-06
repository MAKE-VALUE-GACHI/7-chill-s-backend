package gachi.chills.domain.user.service

import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.topic.domain.repository.TopicRepository
import gachi.chills.domain.user.controller.request.EditMyProfileRequest
import gachi.chills.domain.user.controller.response.EditMyProfileResponse
import gachi.chills.domain.user.controller.response.GetMyProfileResponse
import gachi.chills.domain.user.domain.model.UserTopic
import gachi.chills.domain.user.domain.repository.UserRepository
import gachi.chills.domain.user.domain.repository.UserTopicRepository
import gachi.chills.domain.user.domain.repository.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userTopicRepository: UserTopicRepository,
    private val topicRepository: TopicRepository,
) {
    @Transactional(readOnly = true)
    fun getMyProfile(userContext: UserContext): GetMyProfileResponse {
        val user = userRepository.findByIdOrThrow(userContext.id)

        /**
         * 최적화 방안
         *  1. join 해서 List<Topic> 가져오기
         *  2. userTopic 관계 테이블 데이터 정합성이 무조건 보장된다 가정하고 관계 테이블의 topicId 그대로 반환
         */
        val userTopics = userTopicRepository.findAllByUserId(user.id)
        val topics = topicRepository.findAllById(userTopics.map { it.id })

        return GetMyProfileResponse.of(
            user = user,
            topicIds = topics.map { it.id },
        )
    }

    @Transactional
    fun editMyProfile(
        userContext: UserContext,
        request: EditMyProfileRequest,
    ): EditMyProfileResponse {
        val user = userRepository
            .findByIdOrThrow(userContext.id)
            .editName(request.name)
            .let { userRepository.save(it) }

        val topics = topicRepository.findAllById(request.topicIds)

        val originalUserTopics = userTopicRepository.findAllByUserId(user.id)
        val newUserTopics = topics.map { topic ->
            UserTopic(
                user = user,
                topic = topic,
            )
        }

        val mergedUserTopics = userTopicRepository.saveAll(
            (originalUserTopics + newUserTopics).distinctBy { it.topicId }
        )

        return EditMyProfileResponse.of(
            user = user,
            topicIds = mergedUserTopics.map { it.topicId }
        )
    }
}
