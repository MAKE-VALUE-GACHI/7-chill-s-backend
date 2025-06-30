package gachi.chills.domain.auth.service

import gachi.chills.domain.auth.controller.request.LoginAuthRequest
import gachi.chills.domain.auth.controller.response.LoginAuthResponse
import gachi.chills.domain.auth.controller.response.ReissueTokenAuthResponse
import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.auth.service.adapter.OAuthProcessor
import gachi.chills.domain.auth.service.support.TokenIssuer
import gachi.chills.domain.user.domain.model.User
import gachi.chills.domain.user.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val oAuthProcessor: List<OAuthProcessor>,
    private val userRepository: UserRepository,
    private val tokenIssuer: TokenIssuer,
) {
    fun login(request: LoginAuthRequest): LoginAuthResponse {
        return when (request) {
            is LoginAuthRequest.OAuthLoginAuthRequest -> handleOAuthLogin(request)
        }
    }

    private fun handleOAuthLogin(request: LoginAuthRequest.OAuthLoginAuthRequest): LoginAuthResponse {
        val oAuthProcessor = oAuthProcessor.first { it.supports(request.identifier) }
        val oAuthUserInfo = oAuthProcessor.process(request.code)

        val user = userRepository.findByEmail(oAuthUserInfo.email)
            ?: User.signUp(
                email = oAuthUserInfo.email,
                name = oAuthUserInfo.name,
            )
            .let { userRepository.save(it) }

        val (accessToken, refreshToken) = tokenIssuer.issueToken(user.id)

        return LoginAuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    fun reissueToken(refreshToken: String): ReissueTokenAuthResponse {
        val identifier = tokenIssuer.getIdentifier(refreshToken)
        val (accessToken, newRefreshToken) = tokenIssuer.issueToken(identifier)

        return ReissueTokenAuthResponse(
            accessToken = accessToken,
            refreshToken = newRefreshToken,
        )
    }

    fun logout(userContext: UserContext) {
        tokenIssuer.deleteRefreshToken(userContext.id)
    }
}
