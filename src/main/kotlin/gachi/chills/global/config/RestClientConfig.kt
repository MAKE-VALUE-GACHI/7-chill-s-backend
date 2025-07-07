package gachi.chills.global.config

import gachi.chills.global.exception.BusinessException
import gachi.chills.global.exception.GlobalExceptionCode
import gachi.chills.global.log.logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.net.http.HttpClient
import java.time.Duration


@Configuration
class RestClientConfig {
    private val log = logger()

    @Bean
    fun restClient(): RestClient {
        return RestClient.builder()
            .requestFactory(
                getClientHttpRequestFactory(),
            )
            .defaultStatusHandler(HttpStatusCode::is4xxClientError, handle4xxError())
            .defaultStatusHandler(HttpStatusCode::is5xxServerError, handle5xxError())
            .build()
    }

    private fun getClientHttpRequestFactory(): JdkClientHttpRequestFactory {
        return JdkClientHttpRequestFactory(
            HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT_IN_SEC))
                .build()
        ).apply {
            this.setReadTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT_IN_SEC))
        }
    }

    private fun handle4xxError(): RestClient.ResponseSpec.ErrorHandler {
        return RestClient.ResponseSpec.ErrorHandler { request: HttpRequest, response: ClientHttpResponse ->
            val responseBody = String(response.body.readBytes())
            log.info(
                "[API Client 4xx Error] >> Endpoint={}, RequestHeaders={}, ResponseBody={}",
                "${request.method} ${request.uri}",
                request.headers,
                responseBody,
            )
        }
    }

    private fun handle5xxError(): RestClient.ResponseSpec.ErrorHandler {
        return RestClient.ResponseSpec.ErrorHandler { request: HttpRequest, response: ClientHttpResponse ->
            val responseBody = String(response.body.readBytes())

            log.error(
                "[API Client 5xx Error] >> Endpoint={}, RequestHeaders={}, ResponseBody={}",
                "${request.method} ${request.uri}",
                request.headers,
                responseBody,
            )
            throw BusinessException(GlobalExceptionCode.UNEXPECTED_SERVER_ERROR)
        }
    }

    companion object {
        private const val CONNECTION_TIMEOUT_IN_SEC = 5L
    }
}
