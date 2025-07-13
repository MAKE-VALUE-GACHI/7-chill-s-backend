package gachi.chills.domain.post.domain.repository

import gachi.chills.domain.post.domain.model.Post
import gachi.chills.domain.post.exception.PostExceptionCode
import gachi.chills.global.base.BaseJpaRepository
import gachi.chills.global.exception.BusinessException
import org.springframework.data.repository.findByIdOrNull

interface PostRepository : BaseJpaRepository<Post, Long> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: String): List<Post>
}

fun PostRepository.findByIdOrThrow(id: Long): Post {
    return findByIdOrNull(id) ?: throw BusinessException(
        code = PostExceptionCode.POST_NOT_FOUND,
        detail = "id=$id",
    )
}
