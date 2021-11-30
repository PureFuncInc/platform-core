package net.purefunc.member.external.impl

import net.purefunc.member.data.vo.JwtToken
import net.purefunc.member.domain.data.entity.Member
import net.purefunc.member.domain.data.type.Status
import net.purefunc.member.external.OAuthClient
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import java.util.UUID

class FacebookOAuthClient(
    private val webClient: WebClient,
) : OAuthClient {

    override fun fetch(accessToken: String, jwtTtlSeconds: Long) =
        "https://graph.facebook.com/me?fields=name,email&access_token=$accessToken".getFrom(webClient)
            .let {
                genMemberBy(it["name"].toString(), jwtTtlSeconds, it["email"].toString())
            }

    private fun String.getFrom(webClient: WebClient) = webClient.get()
        .uri(this)
        .retrieve()
        .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
        .block()

    private fun genMemberBy(
        name: String,
        ttlSeconds: Long,
        email: String,
    ) = run {
        val token = JwtToken.generate(
            id = UUID.randomUUID().toString(),
            subject = name,
            issueAt = System.currentTimeMillis(),
            expiration = System.currentTimeMillis() + (ttlSeconds * 60L * 60L * 1000L),
        )

        Member(
            id = null,
            token = token,
            name = name,
            email = email,
            role = "USER",
            status = Status.ACTIVE,
            lastLoginDate = System.currentTimeMillis(),
        )
    }
}