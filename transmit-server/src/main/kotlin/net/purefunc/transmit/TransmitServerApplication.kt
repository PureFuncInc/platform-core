package net.purefunc.transmit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransmitServerApplication

fun main(args: Array<String>) {
    runApplication<TransmitServerApplication>(*args)
}