package gachi.chills.global.filter

import gachi.chills.global.exception.BusinessException
import gachi.chills.global.exception.BusinessExceptionCode
import gachi.chills.global.exception.ExceptionResponse
import gachi.chills.global.exception.GlobalExceptionCode
import gachi.chills.global.log.logger
import gachi.chills.global.util.JsonUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Order(1)
@Component
class AuthenticationExceptionTranslationFilter : OncePerRequestFilter() {
    private val log = logger()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        runCatching {
            filterChain.doFilter(request, response)
        }.onFailure { ex ->
            when (ex) {
                is BusinessException -> {
                    val exceptionResponse = ExceptionResponse.of(request = request, exceptionCode = ex.code)
                    logging(
                        title = "'BusinessException' in Filter",
                        exceptionResponse = exceptionResponse
                    )
                    sendResponse(
                        response = response,
                        exceptionCode = ex.code,
                        exceptionResponse = exceptionResponse,
                    )
                }

                is Exception -> {
                    val exceptionCode = GlobalExceptionCode.UNEXPECTED_SERVER_ERROR
                    val exceptionResponse = ExceptionResponse.of(
                        request = request,
                        exceptionCode = exceptionCode,
                        detail = ex.message,
                    )
                    logging(
                        title = "'Exception' in Filter",
                        exceptionResponse = exceptionResponse
                    )
                    sendResponse(
                        response = response,
                        exceptionCode = exceptionCode,
                        exceptionResponse = ExceptionResponse.of(
                            request = request,
                            exceptionCode = exceptionCode,
                            detail = ex.message,
                        ),
                    )
                }
            }
        }
    }

    private fun logging(
        title: String,
        exceptionResponse: ExceptionResponse,
    ) {
        log.debug(
            "[{}] >> Path='{}', ErrorCode='{}', Message='{}', Detail='{}'",
            title,
            exceptionResponse.path,
            exceptionResponse.exceptionCode,
            exceptionResponse.message,
            exceptionResponse.detail,
        )
    }

    private fun sendResponse(
        response: HttpServletResponse,
        exceptionCode: BusinessExceptionCode,
        exceptionResponse: ExceptionResponse,
    ) {
        response.status = exceptionCode.status.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.writer.write(JsonUtil.writeAsString(exceptionResponse))
    }
}
