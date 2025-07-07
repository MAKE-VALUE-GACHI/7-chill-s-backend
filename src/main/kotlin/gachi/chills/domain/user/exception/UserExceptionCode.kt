package gachi.chills.domain.user.exception

import gachi.chills.global.exception.BusinessExceptionCode
import org.springframework.http.HttpStatus

enum class UserExceptionCode(
    override val status: HttpStatus,
    override val message: String,
) : BusinessExceptionCode {
    USER_NOT_FOUND(
        status = HttpStatus.NOT_FOUND,
        message = "User not found",
    )
}
