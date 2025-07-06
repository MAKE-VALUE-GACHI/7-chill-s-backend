package gachi.chills.domain.auth.exception

import gachi.chills.global.exception.BusinessExceptionCode
import org.springframework.http.HttpStatus

enum class AuthExceptionCode(
    override val status: HttpStatus,
    override val message: String,
) : BusinessExceptionCode {
    AUTH_REQUIRED(
        status = HttpStatus.UNAUTHORIZED,
        message = "인증이 필요합니다.",
    ),
    PERMISSION_DENIED(
        status = HttpStatus.FORBIDDEN,
        message = "권한이 없습니다."
    )
}
