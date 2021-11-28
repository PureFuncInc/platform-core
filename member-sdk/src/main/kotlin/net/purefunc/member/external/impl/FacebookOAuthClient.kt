package net.purefunc.member.external.impl

import net.purefunc.member.ext.genMemberBy
import net.purefunc.member.ext.getFrom
import net.purefunc.member.external.OAuthClient
import org.springframework.web.reactive.function.client.WebClient

class FacebookOAuthClient(
    private val webClient: WebClient,
) : OAuthClient {

    override fun fetch(accessToken: String, jwtTtlSeconds: Long) =
        "https://graph.facebook.com/me?fields=name,email&access_token=$accessToken".getFrom(webClient)
            .let {
                genMemberBy(it["name"].toString(), jwtTtlSeconds, it["email"].toString())
            }
}