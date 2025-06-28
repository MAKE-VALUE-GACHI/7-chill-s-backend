package gachi.chills.domain.like.domain.model

import gachi.chills.domain.post.domain.model.Post
import gachi.chills.domain.user.domain.model.User
import gachi.chills.global.base.HardDeleteNumericBaseJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "likes")
class Like(
    user: User,
    post: Post,
) : HardDeleteNumericBaseJpaEntity() {
    @Column(name = "user_id", nullable = false, columnDefinition = "CHAR(32)")
    val userId: String = user.id

    @Column(name = "post_id", nullable = false)
    val postId: Long = post.id
}
