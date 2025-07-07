package gachi.chills.global.exception

class BusinessException(
    val code: BusinessExceptionCode,
    val detail: String? = null,
) : RuntimeException("${code.message} [$detail]")
