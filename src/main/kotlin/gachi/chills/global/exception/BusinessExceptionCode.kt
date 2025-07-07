package gachi.chills.global.exception

import org.springframework.http.HttpStatus

interface BusinessExceptionCode {
    val status: HttpStatus
    val message: String
}

fun getExceptionCodeEnumName(code: BusinessExceptionCode): String {
    return when (code) {
        is Enum<*> -> code.name
        else -> throw IllegalArgumentException("The provided `BusinessExceptionCode` is not an Enum.")
    }
}
