package gachi.chills.domain.post.domain.model

import gachi.chills.domain.topic.domain.model.Topic
import gachi.chills.global.base.HardDeleteNumericBaseJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "post_topics")
class PostTopic(
    post: Post,
    topic: Topic,
) : HardDeleteNumericBaseJpaEntity() {
    @Column(name = "post_id", nullable = false)
    val postId: Long = post.id

    @Column(name = "topic_id", nullable = false)
    val topicId: Long = topic.id
}
