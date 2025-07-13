package gachi.chills.domain.post.controller

import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.post.service.PostService
import gachi.chills.domain.post.controller.request.PublishPostRequest
import gachi.chills.domain.post.controller.response.MyPostsResponse
import gachi.chills.domain.post.controller.response.PublishPostResponse
import gachi.chills.global.annotation.Auth
import gachi.chills.global.aop.AccessControl
import gachi.chills.global.aop.Allowed
import gachi.chills.global.dto.ResponseWrapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/posts")
class PostController(
    private val postService: PostService,
) : PostControllerDocs {
    @AccessControl(Allowed.AUTHENTICATED)
    @PostMapping
    override fun publish(
        @Auth userContext: UserContext,
        @RequestBody request: PublishPostRequest,
    ): ResponseEntity<PublishPostResponse> {
        val result = postService.publish(userContext, request)
        return ResponseEntity.ok(result)
    }

    @AccessControl(Allowed.AUTHENTICATED)
    @GetMapping("/me")
    override fun getMyPosts(
        @Auth userContext: UserContext,
    ): ResponseEntity<ResponseWrapper<List<MyPostsResponse>>> {
        val result = postService.getMyPosts(userContext)
        return ResponseEntity.ok(ResponseWrapper(result))
    }
}

@Tag(
    name = "게시글 API",
    description = "게시글과 관련된 API 입니다.",
)
internal interface PostControllerDocs {
    @Operation(
        summary = "게시글 작성",
        description = "게시글을 작성합니다.",
    )
    fun publish(
        @Auth userContext: UserContext,
        @RequestBody request: PublishPostRequest,
    ): ResponseEntity<PublishPostResponse>

    @Operation(
        summary = "내가 작성한 게시글 조회",
        description = "내가 작성한 게시글을 조회합니다.",
    )
    fun getMyPosts(
        @Auth userContext: UserContext,
    ): ResponseEntity<ResponseWrapper<List<MyPostsResponse>>>
}
