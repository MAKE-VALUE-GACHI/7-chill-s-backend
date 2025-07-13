package gachi.chills.domain.like.controller

import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.like.service.LikeService
import gachi.chills.global.annotation.Auth
import gachi.chills.global.aop.AccessControl
import gachi.chills.global.aop.Allowed
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LikeController(
    private val likeService: LikeService,
) : LikeControllerDocs {
    @AccessControl(Allowed.AUTHENTICATED)
    @PostMapping("/v1/posts/{id}/likes")
    override fun likePost(
        @Auth userContext: UserContext,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        likeService.likePost(
            userContext = userContext,
            postId = id,
        )
        return ResponseEntity.noContent().build()
    }

    @AccessControl(Allowed.AUTHENTICATED)
    @DeleteMapping("/v1/posts/{id}/likes")
    override fun cancelLikePost(
        @Auth userContext: UserContext,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        likeService.cancelLikePost(
            userContext = userContext,
            postId = id,
        )
        return ResponseEntity.noContent().build()
    }
}

@Tag(
    name = "게시글 좋아요 API",
    description = "게시글 좋아요와 관련된 API 입니다.",
)
internal interface LikeControllerDocs {
    @Operation(
        summary = "게시글 좋아요",
        description = "게시글을 좋아요 처리합니다.",
    )
    fun likePost(
        @Auth userContext: UserContext,
        @PathVariable id: Long,
    ): ResponseEntity<Unit>

    @Operation(
        summary = "게시글 좋아요 취소",
        description = "게시글을 좋아요 취소 처리합니다.",
    )
    fun cancelLikePost(
        @Auth userContext: UserContext,
        @PathVariable id: Long,
    ): ResponseEntity<Unit>
}
