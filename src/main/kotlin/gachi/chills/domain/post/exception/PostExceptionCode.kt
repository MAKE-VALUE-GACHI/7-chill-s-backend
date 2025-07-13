package gachi.chills.domain.post.exception

import gachi.chills.global.exception.BusinessExceptionCode
import org.springframework.http.HttpStatus

enum class PostExceptionCode(
    override val status: HttpStatus,
    override val message: String,
) : BusinessExceptionCode {
    POST_NOT_FOUND(
        status = HttpStatus.NOT_FOUND,
        message = "Post not found",
    )
}
