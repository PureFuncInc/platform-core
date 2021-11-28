package net.purefunc.member.ext

import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient

fun String.getFrom(webClient: WebClient) = webClient.get()
    .uri(this)
    .retrieve()
    .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
    .block()