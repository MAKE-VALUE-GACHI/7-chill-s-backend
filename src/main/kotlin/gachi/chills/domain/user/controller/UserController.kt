package gachi.chills.domain.user.controller

import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.user.controller.request.EditMyProfileRequest
import gachi.chills.domain.user.controller.response.EditMyProfileResponse
import gachi.chills.domain.user.controller.response.GetMyProfileResponse
import gachi.chills.global.annotation.Auth
import gachi.chills.global.aop.AccessControl
import gachi.chills.global.aop.Allowed
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController : UserControllerDocs {
    @GetMapping("/me")
    override fun getMyProfile(
        @Auth userContext: UserContext,
    ): ResponseEntity<GetMyProfileResponse> {
        TODO("Not yet implemented")
    }

    @PatchMapping
    @AccessControl(Allowed.AUTHENTICATED)
    override fun editMyProfile(
        @Auth userContext: UserContext,
        @RequestBody @Valid request: EditMyProfileRequest,
    ): ResponseEntity<EditMyProfileResponse> {
        TODO("Not yet implemented")
    }
}

@Tag(
    name = "유저 API",
    description = "유저와 관련된 API 입니다.",
)
internal interface UserControllerDocs {
    @Operation(
        summary = "내 프로필 조회",
        description = "내 프로필을 조회합니다.",
    )
    fun getMyProfile(
        @Auth userContext: UserContext,
    ): ResponseEntity<GetMyProfileResponse>

    @Operation(
        summary = "내 프로필 수정",
        description = "내 프로필을 수정합니다.",
    )
    fun editMyProfile(
        @Auth userContext: UserContext,
        @RequestBody @Valid request: EditMyProfileRequest,
    ): ResponseEntity<EditMyProfileResponse>
}
