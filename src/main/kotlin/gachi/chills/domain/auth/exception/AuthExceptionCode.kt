package gachi.chills.domain.auth.exception

import gachi.chills.global.exception.BusinessExceptionCode
import org.springframework.http.HttpStatus

enum class AuthExceptionCode(
    override val message: String,
    override val status: HttpStatus,
) : BusinessExceptionCode {
    AUTH_REQUIRED(
        message = "인증이 필요합니다.",
        status = HttpStatus.UNAUTHORIZED,
    )
}
