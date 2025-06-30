package gachi.chills.global.exception

import jakarta.servlet.http.HttpServletRequest
import java.time.LocalDateTime

data class ExceptionResponse private constructor(
    val exceptionCode: String,
    val message: String,
    val detail: String?,
    val path: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun of(
            request: HttpServletRequest,
            exceptionCode: BusinessExceptionCode,
            detail: String? = null,
        ): ExceptionResponse {
            return ExceptionResponse(
                exceptionCode = getExceptionCodeEnumName(exceptionCode),
                message = exceptionCode.message,
                detail = detail,
                path = "${request.method} ${request.requestURI}",
            )
        }

        fun of(
            request: HttpServletRequest,
            exception: BusinessException,
        ): ExceptionResponse {
            return ExceptionResponse(
                exceptionCode = getExceptionCodeEnumName(exception.code),
                message = exception.code.message,
                detail = exception.detail,
                path = "${request.method} ${request.requestURI}",
            )
        }
    }
}
