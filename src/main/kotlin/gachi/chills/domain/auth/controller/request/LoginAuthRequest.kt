package gachi.chills.domain.auth.controller.request

/**
 * TODO:
 *  - 추후 패스워드 기반 로그인 DTO 추가
 *  - 패스워드 기반 Request 만 Swagger 에 노출되도록 설정
 */
sealed interface LoginAuthRequest {
    data class OAuthLoginAuthRequest(
        val identifier: String,
        val code: String,
    ) : LoginAuthRequest
}
