package net.purefunc.member.external.impl

import net.purefunc.member.ext.genMemberBy
import net.purefunc.member.ext.getFrom
import net.purefunc.member.external.OAuthClient
import org.springframework.web.reactive.function.client.WebClient

class FacebookOAuthClient(
    private val webClient: WebClient,
) : OAuthClient {

    override fun fetch(code: String, ttlSeconds: Long) =
        "https://graph.facebook.com/me?fields=name,email&access_token=$code".getFrom(webClient)
            .let {
                genMemberBy(it["name"].toString(), ttlSeconds, it["email"].toString())
            }
}