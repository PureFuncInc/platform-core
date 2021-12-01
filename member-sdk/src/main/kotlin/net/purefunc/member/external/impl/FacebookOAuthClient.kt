package net.purefunc.member.external.impl

import arrow.core.Either
import net.purefunc.member.MemberFunc
import net.purefunc.member.external.OAuthClient
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient

class FacebookOAuthClient(
    private val webClient: WebClient,
) : OAuthClient, MemberFunc() {

    override suspend fun fetch(accessToken: String, jwtTtlSeconds: Long) =
        Either.catch {
            "https://graph.facebook.com/me?fields=name,email&access_token=$accessToken".getFrom(webClient)
                .let {
                    genMemberBy(
                        name = it["name"].toString(),
                        ttlSeconds = jwtTtlSeconds,
                        email = it["email"].toString(),
                    )
                }
        }

    private fun String.getFrom(webClient: WebClient) = webClient.get()
        .uri(this)
        .retrieve()
        .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
        .block()
}