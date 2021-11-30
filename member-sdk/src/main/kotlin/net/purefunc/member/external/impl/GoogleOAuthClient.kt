package net.purefunc.member.external.impl

import arrow.core.Either
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

    override suspend fun fetch(accessToken: String, jwtTtlSeconds: Long) =
        Either.catch {
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