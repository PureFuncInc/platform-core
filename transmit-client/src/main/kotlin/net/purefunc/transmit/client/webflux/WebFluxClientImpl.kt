package net.purefunc.transmit.client.webflux

import net.purefunc.transmit.client.TransmitClient
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient

class WebFluxClientImpl(
    private val webClient: WebClient
) : TransmitClient {

    override fun sendEmail(email: String, content: String) =
        webClient.post()
            .uri("/api/v1/transmit/email")
            .bodyValue(mapOf("email" to email, "content" to content))
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
            .block()["code"]
            .toString()

    override fun sendSms(phone: String, content: String) =
        webClient.post()
            .uri("/api/v1/transmit/sms")
            .bodyValue(mapOf("phone" to phone, "content" to content))
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
            .block()["code"]
            .toString()

    override fun makePhoneCall(phone: String, content: String) =
        webClient.post()
            .uri("/api/v1/transmit/phone")
            .bodyValue(mapOf("phone" to phone, "content" to content))
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
            .block()["code"]
            .toString()
}