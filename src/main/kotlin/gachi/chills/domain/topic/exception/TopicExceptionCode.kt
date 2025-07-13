package gachi.chills.domain.topic.exception

import gachi.chills.global.exception.BusinessExceptionCode
import org.springframework.http.HttpStatus

enum class TopicExceptionCode(
    override val status: HttpStatus,
    override val message: String,
) : BusinessExceptionCode {
    TOPIC_NOT_FOUND(
        status = HttpStatus.NOT_FOUND,
        message = "Topic not found",
    )
}
