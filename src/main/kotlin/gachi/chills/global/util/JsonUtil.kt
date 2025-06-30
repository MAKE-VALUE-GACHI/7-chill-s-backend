package gachi.chills.global.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

class JsonUtil(
    _objectMapper: ObjectMapper,
) {
    init {
        objectMapper = _objectMapper
    }

    companion object {
        lateinit var objectMapper: ObjectMapper

        fun isJsonFormat(json: String): Boolean {
            return try {
                objectMapper.readTree(json)
                true
            } catch (_: JsonProcessingException) {
                false
            }
        }

        fun writeAsString(data: Any): String {
            return objectMapper.writeValueAsString(data)
        }

        inline fun <reified T : Any> readAsSpecificType(data: String): T {
            return objectMapper.readValue(data, object : TypeReference<T>() {})
        }
    }
}
