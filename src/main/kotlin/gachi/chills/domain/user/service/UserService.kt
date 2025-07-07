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
            topics = topics,
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

        /**
         * 최적화 방안
         *  - 토픽 수가 많지 않을 거라 판단하여 기존 관계 삭제 후 새로 저장하는 플로우로 구현
         *  - 만약 토픽 수가 많아질 경우 `@Modifying` + `JPQL`을 활용하거나 native query 활용 필요
         */
        val userTopics = topics.map { topic ->
            UserTopic(
                user = user,
                topic = topic,
            )
        }.let { userTopics ->
            // 기존 관계 삭제
            userTopicRepository.deleteAllByUserId(user.id)

            // 요청받은 토픽으로만 관계 생성
            userTopicRepository.saveAll(userTopics)
        }

        return EditMyProfileResponse.of(
            user = user,
            topicIds = userTopics.map { it.topicId }
        )
    }
}
