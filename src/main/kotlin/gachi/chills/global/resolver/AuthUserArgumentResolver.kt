package gachi.chills.global.resolver

import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.auth.domain.repository.UserContextHolder
import gachi.chills.domain.auth.exception.AuthExceptionCode
import gachi.chills.global.annotation.Auth
import gachi.chills.global.exception.BusinessException
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AuthUserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasRequiredAnnotation: Boolean = parameter.getParameterAnnotation(Auth::class.java) != null
        val hasRequiredType: Boolean = parameter.parameterType == UserContext::class.java
        return hasRequiredAnnotation && hasRequiredType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): UserContext {
        return UserContextHolder.get()
            ?: throw BusinessException(AuthExceptionCode.AUTH_REQUIRED)
    }
}
