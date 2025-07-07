package gachi.chills.global.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import gachi.chills.global.log.logger
import gachi.chills.global.util.JsonUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.MultipartException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = logger()

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        request: HttpServletRequest,
        exception: BusinessException,
    ): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse.of(request = request, exception = exception)
        infoLogging(response)
        return makeResponse(exceptionCode = exception.code, response = response)
    }

    @ExceptionHandler(
        IllegalArgumentException::class,
        IllegalStateException::class,
    )
    fun handleKotlinRequiredAndCheckException(
        request: HttpServletRequest,
        exception: Exception,
    ): ResponseEntity<ExceptionResponse> {
        val exceptionCode = GlobalExceptionCode.VALIDATION_ERROR
        val response = ExceptionResponse.of(
            request = request,
            exceptionCode = exceptionCode,
            detail = exception.message,
        )
        infoLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(
        request: HttpServletRequest,
        exception: NoHandlerFoundException,
    ): ResponseEntity<ExceptionResponse> {
        val exceptionCode = GlobalExceptionCode.NOT_SUPPORTED_URI_ERROR
        val response = ExceptionResponse.of(
            request = request,
            exceptionCode = exceptionCode,
            detail = null,
        )
        warnLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    /**
     * HTTP Body Data Parsing에 대한 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        request: HttpServletRequest,
        exception: HttpMessageNotReadableException,
    ): ResponseEntity<ExceptionResponse> {
        val exceptionCode = GlobalExceptionCode.VALIDATION_ERROR
        val response = ExceptionResponse.of(
            request = request,
            exceptionCode = exceptionCode,
            detail = exception.message,
        )
        warnLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    @ExceptionHandler(MismatchedInputException::class)
    fun handleMismatchedInputException(
        request: HttpServletRequest,
        exception: MismatchedInputException,
    ): ResponseEntity<ExceptionResponse> {
        val exceptionCode = GlobalExceptionCode.VALIDATION_ERROR
        val response = ExceptionResponse.of(
            request = request,
            exceptionCode = exceptionCode,
            detail = exception.message,
        )
        warnLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    @ExceptionHandler(
        UnsatisfiedServletRequestParameterException::class,
        MissingServletRequestPartException::class,
        MissingServletRequestParameterException::class,
        MultipartException::class,
    )
    fun handleUnsatisfiedServletRequestParameterException(
        request: HttpServletRequest,
        exception: Exception,
    ): ResponseEntity<ExceptionResponse> {
        val exceptionCode = GlobalExceptionCode.VALIDATION_ERROR
        val response = ExceptionResponse.of(
            request = request,
            exceptionCode = exceptionCode,
            detail = exception.message,
        )
        infoLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        request: HttpServletRequest,
        exception: MethodArgumentNotValidException,
    ): ResponseEntity<ExceptionResponse> {
        val invalidFieldsMsgs = extractFieldErrorMessage(exception.fieldErrors)
        val exceptionCode = GlobalExceptionCode.VALIDATION_ERROR
        val response = ExceptionResponse.of(
            request = request,
            exceptionCode = exceptionCode,
            detail = invalidFieldsMsgs.toString(),
        )
        infoLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(
        request: HttpServletRequest,
        exception: BindException,
    ): ResponseEntity<ExceptionResponse> {
        val invalidFieldsMsgs = extractFieldErrorMessage(exception.fieldErrors)
        val exceptionCode = GlobalExceptionCode.VALIDATION_ERROR
        val response = ExceptionResponse.of(
            request = request,
            exceptionCode = exceptionCode,
            detail = invalidFieldsMsgs.toString(),
        )
        infoLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        request: HttpServletRequest,
        exception: ConstraintViolationException,
    ): ResponseEntity<ExceptionResponse> {
        val exceptionCode = GlobalExceptionCode.VALIDATION_ERROR
        val response = ExceptionResponse.of(
            request = request,
            exceptionCode = exceptionCode,
            detail = exception.message,
        )
        infoLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(
        request: HttpServletRequest,
        exception: MethodArgumentTypeMismatchException,
    ): ResponseEntity<ExceptionResponse> {
        val exceptionCode = GlobalExceptionCode.VALIDATION_ERROR
        val response = ExceptionResponse.of(
            request = request,
            exceptionCode = exceptionCode,
            detail = exception.message,
        )
        infoLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(
        request: HttpServletRequest,
        exception: MaxUploadSizeExceededException,
    ): ResponseEntity<ExceptionResponse> {
        val exceptionCode = GlobalExceptionCode.VALIDATION_ERROR
        val response = ExceptionResponse.of(
            request = request,
            exceptionCode = exceptionCode,
            detail = "${exception.body.detail}: allow=${exception.maxUploadSize}",
        )
        infoLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(
        request: HttpServletRequest,
        exception: HttpRequestMethodNotSupportedException,
    ): ResponseEntity<ExceptionResponse> {
        val exceptionCode = GlobalExceptionCode.NOT_SUPPORTED_METHOD_ERROR
        val response = ExceptionResponse.of(request = request, exceptionCode = exceptionCode)
        infoLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleHttpMediaTypeNotSupportedException(
        request: HttpServletRequest,
        exception: HttpMediaTypeNotSupportedException,
    ): ResponseEntity<ExceptionResponse> {
        val exceptionCode = GlobalExceptionCode.UNSUPPORTED_MEDIA_TYPE_ERROR
        val response = ExceptionResponse.of(request = request, exceptionCode = exceptionCode)
        infoLogging(response)
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(
        request: HttpServletRequest,
        exception: Exception,
    ): ResponseEntity<ExceptionResponse> {
        val exceptionCode = GlobalExceptionCode.UNEXPECTED_SERVER_ERROR
        val response = ExceptionResponse.of(request = request, exceptionCode = exceptionCode)
        errorLogging(
            response = response,
            traceMessage = "${exception.message} ${exception.stackTrace.take(5)}",
        )
        return makeResponse(exceptionCode = exceptionCode, response = response)
    }

    private fun extractFieldErrorMessage(fieldErrors: List<FieldError>): String? {
        if (fieldErrors.size == 1) {
            return fieldErrors[0].defaultMessage
        }

        return runCatching {
            JsonUtil.writeAsString(
                fieldErrors.associate {
                    "key=${it.field}" to "reason=${it.defaultMessage}"
                }
            )
        }.onFailure {
            throw RuntimeException("JSON Parsing Error...", it)
        }.getOrThrow()
    }

    private fun infoLogging(response: ExceptionResponse) {
        log.info(
            "[INFO] >> Path='{}', ErrorCode='{}', Message='{}', Detail='{}'",
            response.path,
            response.exceptionCode,
            response.message,
            response.detail,
        )
    }

    private fun warnLogging(response: ExceptionResponse) {
        log.warn(
            "[WARN] >> Path='{}', ErrorCode='{}', Message='{}', Detail='{}'",
            response.path,
            response.exceptionCode,
            response.message,
            response.detail,
        )
    }

    private fun errorLogging(
        response: ExceptionResponse,
        traceMessage: String,
    ) {
        log.error(
            "[ERROR] >> Path='{}', ErrorCode='{}', Message='{}', Detail='{}'",
            response.path,
            response.exceptionCode,
            traceMessage,
            response.detail,
        )
    }

    private fun makeResponse(
        exceptionCode: BusinessExceptionCode,
        response: ExceptionResponse,
    ): ResponseEntity<ExceptionResponse> {
        return ResponseEntity.status(exceptionCode.status.value()).body(response)
    }
}
