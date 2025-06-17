package gachi.chills

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChillsApplication

fun main(args: Array<String>) {
    runApplication<ChillsApplication>(*args)
}
