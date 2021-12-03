package net.purefunc.member.external.impl

import arrow.core.Either.Companion.catch
import net.purefunc.member.MemberFunc
import net.purefunc.member.external.OAuthClient
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient

class FacebookOAuthClient(
    private val webClient: WebClient,
) : OAuthClient, MemberFunc() {

    override suspend fun fetch(accessToken: String, jwtTtlSeconds: Long, role: String) =
        catch {
            "https://graph.facebook.com/me?fields=name,email&access_token=$accessToken".getFrom(webClient)
                .let {
                    initMember(
                        name = it["name"].toString(),
                        ttlSeconds = jwtTtlSeconds,
                        role = role,
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