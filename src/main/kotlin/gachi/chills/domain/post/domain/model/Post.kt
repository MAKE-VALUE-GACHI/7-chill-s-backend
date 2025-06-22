package gachi.chills.domain.post.domain.model

import gachi.chills.domain.user.domain.model.User
import gachi.chills.global.base.HardDeleteNumericBaseJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "posts")
class Post(
    user: User,
    title: String,
    content: String,
    isPublic: Boolean,
): HardDeleteNumericBaseJpaEntity() {
    @Column(name = "user_id", nullable = false)
    val userId: String = user.id

    @Column(name = "title", nullable = false, columnDefinition = "VARCHAR(150)")
    var title: String = title
        private set

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String = content
        private set

    @Column(name = "is_public", nullable = false, columnDefinition = "BIT(1)")
    var isPublic: Boolean = isPublic
        private set
}
