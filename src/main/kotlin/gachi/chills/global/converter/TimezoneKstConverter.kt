package gachi.chills.global.converter

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object KstFormatter {
    val LOCAL_DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val LOCAL_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val ZONE_ID: ZoneId = ZoneId.of("Asia/Seoul")
}

class LocalDateTimeKstSerializer : JsonSerializer<LocalDateTime>() {
    override fun serialize(
        value: LocalDateTime,
        gen: JsonGenerator,
        serializers: SerializerProvider,
    ) {
        val kstString = value.atZone(ZoneId.systemDefault())
            .withZoneSameInstant(KstFormatter.ZONE_ID)
            .toLocalDateTime()
            .format(KstFormatter.LOCAL_DATE_TIME_FORMATTER)

        gen.writeString(kstString)
    }
}

class LocalDateTimeKstDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(
        p: JsonParser,
        ctxt: DeserializationContext,
    ): LocalDateTime {
        val text = p.text
        val parsed = LocalDateTime.parse(text, KstFormatter.LOCAL_DATE_TIME_FORMATTER)
        return parsed.atZone(KstFormatter.ZONE_ID)
            .withZoneSameInstant(ZoneId.systemDefault())
            .toLocalDateTime()
    }
}

class LocalDateKstSerializer : JsonSerializer<LocalDate>() {
    override fun serialize(
        value: LocalDate,
        gen: JsonGenerator,
        serializers: SerializerProvider,
    ) {
        val kstString = value.format(KstFormatter.LOCAL_DATE_FORMATTER)
        gen.writeString(kstString)
    }
}

class LocalDateKstDeserializer : JsonDeserializer<LocalDate>() {
    override fun deserialize(
        p: JsonParser,
        ctxt: DeserializationContext,
    ): LocalDate {
        val text = p.text
        return LocalDate.parse(text, KstFormatter.LOCAL_DATE_FORMATTER)
    }
}
