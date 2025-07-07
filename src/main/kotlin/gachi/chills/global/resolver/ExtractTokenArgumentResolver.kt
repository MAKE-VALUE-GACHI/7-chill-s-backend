package gachi.chills.global.resolver

import gachi.chills.domain.auth.service.support.TokenValidator
import gachi.chills.domain.auth.util.TokenExtractor
import gachi.chills.global.annotation.ExtractToken
import gachi.chills.global.annotation.TokenType
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class ExtractTokenArgumentResolver(
    private val tokenValidator: TokenValidator,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasRequiredAnnotation: Boolean = parameter.getParameterAnnotation(ExtractToken::class.java) != null
        val hasRequiredType: Boolean = parameter.parameterType == String::class.java
        return hasRequiredAnnotation && hasRequiredType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): String {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
        val extractToken = parameter.getParameterAnnotation(ExtractToken::class.java)!!
        return getToken(request, extractToken.type)
    }

    private fun getToken(
        request: HttpServletRequest,
        type: TokenType,
    ): String {
        return when (type) {
            TokenType.ACCESS -> TokenExtractor.extractAccessTokenOrThrow(request).also {
                tokenValidator.validateAccessToken(it)
            }

            TokenType.REFRESH -> TokenExtractor.extractRefreshTokenOrThrow(request).also {
                tokenValidator.validateRefreshToken(it)
            }
        }
    }
}
