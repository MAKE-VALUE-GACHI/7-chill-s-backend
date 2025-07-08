package gachi.chills.domain.like.controller

import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.like.service.LikeService
import gachi.chills.global.annotation.Auth
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LikeController(
    private val likeService: LikeService,
) : LikeControllerDocs {
    @PostMapping("/v1/posts/likes")
    override fun likePost(
        @Auth userContext: UserContext,

    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }
}

internal interface LikeControllerDocs {
    fun likePost(
        @Auth userContext: UserContext,
    ): ResponseEntity<Unit>
}
