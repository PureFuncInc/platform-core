package net.purefunc.transmit.client.webflux

import net.purefunc.common.domain.data.type.BusinessOperation
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import transmit.TransmitService

class WebFluxClientImpl(
    private val webClient: WebClient
) : TransmitService {

    override fun sendEmail(address: String, content: String) =
        webClient.post()
            .uri("/api/v1/transmit/email")
            .bodyValue(mapOf("email" to address, "content" to content))
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
            .block()["code"]
            .toString()
            .toBusinessOperation()


    override fun sendSms(phone: String, content: String) =
        webClient.post()
            .uri("/api/v1/transmit/sms")
            .bodyValue(mapOf("phone" to phone, "content" to content))
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
            .block()["code"]
            .toString()
            .toBusinessOperation()

    override fun makePhoneCall(phone: String, content: String) =
        webClient.post()
            .uri("/api/v1/transmit/phone")
            .bodyValue(mapOf("phone" to phone, "content" to content))
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
            .block()["code"]
            .toString()
            .toBusinessOperation()
}

private fun String.toBusinessOperation() = BusinessOperation("TRANS_200", "TRANSMIT SERIAL NUMBER", this)