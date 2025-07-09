package gachi.chills.global.filter

import gachi.chills.domain.auth.domain.model.toUserContext
import gachi.chills.domain.auth.domain.repository.UserContextHolder
import gachi.chills.domain.auth.service.support.TokenIssuer
import gachi.chills.domain.auth.util.TokenExtractor
import gachi.chills.domain.user.domain.repository.UserRepository
import gachi.chills.domain.user.domain.repository.findByIdOrThrow
import gachi.chills.global.util.isNotNullOrBlank
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Order(2)
@Component
class TokenAuthenticationFilter(
    private val tokenIssuer: TokenIssuer,
    private val userRepository: UserRepository,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (UserContextHolder.get() != null) {
            return filterChain.doFilter(request, response)
        }

        // Todo 화이트리스트 환경변수로 관리 필요.
        val uri = request.requestURI
        if (uri.contains("/swagger-ui")) {
            return filterChain.doFilter(request, response)
        }

        val accessToken = TokenExtractor.extractAccessTokenOrThrow(request)
        val userId = tokenIssuer.getIdentifier(accessToken)

        if (accessToken.isNotNullOrBlank()) {
            val user = userRepository.findByIdOrThrow(userId)
            UserContextHolder.store(user.toUserContext())
        }

        filterChain.doFilter(request, response)
    }
}
