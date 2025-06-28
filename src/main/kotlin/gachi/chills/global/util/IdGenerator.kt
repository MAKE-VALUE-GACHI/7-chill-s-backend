package gachi.chills.global.util

import java.util.UUID

object IdGenerator {
    fun uuid(): String = UUID.randomUUID().toString().replace("-", "")
}
