package gachi.chills.domain.like.service

import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.like.domain.model.Like
import gachi.chills.domain.like.domain.repository.LikeRepository
import gachi.chills.domain.post.domain.repository.PostRepository
import gachi.chills.domain.post.domain.repository.findByIdOrThrow
import gachi.chills.domain.user.domain.repository.UserRepository
import gachi.chills.domain.user.domain.repository.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun likePost(
        userContext: UserContext,
        postId: Long,
    ) {
        val user = userRepository.findByIdOrThrow(userContext.id)
        val post = postRepository.findByIdOrThrow(postId)

        check(likeRepository.findByUserIdAndPostId(user.id, post.id) == null) {
            "이미 좋아요를 누른 게시글입니다."
        }

        val like = Like(
            user = user,
            post = post,
        )

        likeRepository.save(like)
    }

    @Transactional
    fun cancelLikePost(
        userContext: UserContext,
        postId: Long,
    ) {
        val user = userRepository.findByIdOrThrow(userContext.id)
        val post = postRepository.findByIdOrThrow(postId)

        val like = likeRepository.findByUserIdAndPostId(
            userId = user.id,
            postId = post.id,
        ) ?: throw IllegalStateException("좋아요를 누르지 않은 게시글입니다.")

        likeRepository.delete(like)
    }
}
