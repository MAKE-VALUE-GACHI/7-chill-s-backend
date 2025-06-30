package gachi.chills.global.exception

import org.springframework.http.HttpStatus

enum class GlobalExceptionCode(
    override val status: HttpStatus,
    override val message: String,
) : BusinessExceptionCode {
    NOT_SUPPORTED_URI_ERROR(HttpStatus.NOT_FOUND, "잘못된 요청입니다."),
    NOT_SUPPORTED_METHOD_ERROR(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 요청입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNSUPPORTED_MEDIA_TYPE_ERROR(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "잘못된 요청입니다."),
    UNEXPECTED_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류입니다."),
}
