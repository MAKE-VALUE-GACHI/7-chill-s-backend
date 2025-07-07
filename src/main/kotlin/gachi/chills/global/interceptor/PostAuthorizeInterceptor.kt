package gachi.chills.global.interceptor

import gachi.chills.domain.auth.domain.repository.UserContextHolder
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView

class PostAuthorizeInterceptor : HandlerInterceptor {
    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?,
    ) {
        UserContextHolder.clear()
        super.postHandle(request, response, handler, modelAndView)
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        UserContextHolder.clear()
        super.afterCompletion(request, response, handler, ex)
    }
}
