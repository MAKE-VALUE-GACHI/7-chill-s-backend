package gachi.chills.global.util

import io.jsonwebtoken.security.Keys
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.URI
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun String?.isNotNullOrBlank(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrBlank != null)
    }
    return !this.isNullOrBlank()
}

fun String.toSecretKey(): SecretKey {
    return Keys.hmacShaKeyFor(this.toByteArray(StandardCharsets.UTF_8))
}

fun String.toURI(): URI {
    return URI.create(this)
}

fun redirect(location: URI): ResponseEntity<Unit> {
    return ResponseEntity<Unit>(
        HttpHeaders().apply {
            this.location = location
        },
        HttpStatus.PERMANENT_REDIRECT,
    )
}
