package net.purefunc.member.external.impl

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import net.purefunc.core.ext.Slf4j
import net.purefunc.member.data.vo.JwtToken
import net.purefunc.member.domain.data.entity.Member
import net.purefunc.member.domain.data.type.Status
import net.purefunc.member.external.OAuthClient
import java.util.UUID

@Slf4j
class GoogleOAuthClient(
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String,
) : OAuthClient {

    override fun fetch(accessToken: String, jwtTtlSeconds: Long) =
        GoogleAuthorizationCodeTokenRequest(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            clientId,
            clientSecret,
            accessToken,
            redirectUri,
        ).execute().parseIdToken().payload
            .let {
                genMemberBy(it["name"].toString(), jwtTtlSeconds, it["email"].toString())
            }

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

//fun main() {
//    GoogleOAuthClient(
//        clientId = "820332047932-3chgneoe75ef6nv1o3d0hlh8spkiur52.apps.googleusercontent.com",
//        clientSecret = "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",
//        redirectUri = "http://localhost:8080",
//    ).fetch("4/0AX4XfWjlXoQ7RydkszEmLBx_yrF2mwoOItjvGpvOwxjTm6Ie0ItIMu4FDd8ci-n7woM4lw", 60L* 60L)
//}