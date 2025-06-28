package gachi.chills.domain.user.domain.model

import gachi.chills.domain.topic.domain.model.Topic
import gachi.chills.global.base.HardDeleteNumericBaseJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "user_topics")
class UserTopic(
    user: User,
    topic: Topic,
) : HardDeleteNumericBaseJpaEntity() {
    @Column(name = "user_id", nullable = false, columnDefinition = "CHAR(32)")
    val userId: String = user.id

    @Column(name = "topic_id", nullable = false)
    val topicId: Long = topic.id
}
