package net.purefunc.transmit.web.controller

import net.purefunc.common.domain.data.Failure
import net.purefunc.common.domain.data.Success
import net.purefunc.common.domain.data.otherwise
import net.purefunc.common.ext.Slf4j
import net.purefunc.common.ext.Slf4j.Companion.log
import net.purefunc.transmit.external.EmailClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Slf4j
@RestController
@RequestMapping("/api/v1.0")
class TransmitController(
    private val gmailClient: EmailClient
) {

    @PostMapping("/email")
    fun sendEmail(@RequestBody params: Map<String, String>) =
        run {
            val send = gmailClient.send(
                subject = params["subject"].toString(),
                personal = params["personal"].toString(),
                address = params["address"].toString(),
                htmlContent = params["htmlContent"].toString(),
            )
            when (send) {
                is Success -> Mono.just(ResponseEntity.ok(send))
                is Failure -> {
                    send.otherwise { log.error(it.message, it) }
                    Mono.just(ResponseEntity.internalServerError().build())
                }
            }
        }
}